const STORAGE_KEY = "rural-dispatcher-v1";

const initialState = {
  nextRideNumber: 101,
  riders: [],
  rides: [],
  activeFilter: "all"
};

let state = loadState();

const els = {
  rideForm: document.querySelector("#rideForm"),
  riderForm: document.querySelector("#riderForm"),
  addRiderButton: document.querySelector("#addRiderButton"),
  riderList: document.querySelector("#riderList"),
  rideBoard: document.querySelector("#rideBoard"),
  rideCounter: document.querySelector("#rideCounter"),
  seedDemo: document.querySelector("#seedDemo"),
  exportData: document.querySelector("#exportData"),
  clearDone: document.querySelector("#clearDone"),
  distance: document.querySelector("#distance"),
  fare: document.querySelector("#fare")
};

els.rideForm.addEventListener("submit", createRide);
els.riderForm.addEventListener("submit", createRider);
els.addRiderButton.addEventListener("click", () => {
  els.riderForm.classList.toggle("hidden");
});
els.seedDemo.addEventListener("click", loadDemoData);
els.exportData.addEventListener("click", exportCsv);
els.clearDone.addEventListener("click", clearCompleted);
els.distance.addEventListener("input", suggestFare);
document.querySelectorAll(".tab").forEach((button) => {
  button.addEventListener("click", () => {
    state.activeFilter = button.dataset.filter;
    saveState();
    render();
  });
});

render();

function loadState() {
  try {
    const saved = JSON.parse(localStorage.getItem(STORAGE_KEY));
    return saved ? { ...initialState, ...saved } : structuredClone(initialState);
  } catch {
    return structuredClone(initialState);
  }
}

function saveState() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
}

function createRide(event) {
  event.preventDefault();
  const form = new FormData(event.currentTarget);
  const ride = {
    id: `R${state.nextRideNumber}`,
    createdAt: new Date().toISOString(),
    passengerName: clean(form.get("passengerName")),
    passengerPhone: clean(form.get("passengerPhone")),
    pickup: clean(form.get("pickup")),
    drop: clean(form.get("drop")),
    time: clean(form.get("time")),
    distance: Number(form.get("distance")),
    fare: Number(form.get("fare")),
    seats: Number(form.get("seats")),
    notes: clean(form.get("notes")),
    status: "new",
    riderId: ""
  };

  state.nextRideNumber += 1;
  state.rides.unshift(ride);
  saveState();
  event.currentTarget.reset();
  els.distance.value = 10;
  els.fare.value = 80;
  render();
}

function createRider(event) {
  event.preventDefault();
  const form = new FormData(event.currentTarget);
  state.riders.push({
    id: crypto.randomUUID(),
    name: clean(form.get("riderName")),
    phone: clean(form.get("riderPhone")),
    base: clean(form.get("riderBase")),
    vehicle: clean(form.get("vehicle")),
    available: true
  });
  saveState();
  event.currentTarget.reset();
  els.riderForm.classList.add("hidden");
  render();
}

function render() {
  renderTabs();
  renderRiders();
  renderRides();
  els.rideCounter.textContent = `${state.rides.length} rides today`;
}

function renderTabs() {
  document.querySelectorAll(".tab").forEach((button) => {
    button.classList.toggle("active", button.dataset.filter === state.activeFilter);
  });
}

function renderRiders() {
  els.riderList.innerHTML = "";
  if (!state.riders.length) {
    els.riderList.append(emptyNode("Add your first verified rider."));
    return;
  }

  state.riders.forEach((rider) => {
    const row = document.createElement("div");
    row.className = "rider-row";
    row.innerHTML = `
      <div class="rider-meta">
        <span class="rider-name">${escapeHtml(rider.name)}</span>
        <span class="rider-sub">${escapeHtml(rider.base)} · ${escapeHtml(rider.vehicle)}</span>
        <span class="rider-sub">${escapeHtml(rider.phone)}</span>
      </div>
      <label class="toggle">
        <input type="checkbox" ${rider.available ? "checked" : ""}>
        Available
      </label>
    `;
    row.querySelector("input").addEventListener("change", (event) => {
      rider.available = event.target.checked;
      saveState();
      render();
    });
    els.riderList.append(row);
  });
}

function renderRides() {
  els.rideBoard.innerHTML = "";
  const rides = state.rides.filter((ride) => {
    return state.activeFilter === "all" || ride.status === state.activeFilter;
  });

  if (!rides.length) {
    els.rideBoard.append(emptyNode("No rides in this view."));
    return;
  }

  rides.forEach((ride) => els.rideBoard.append(renderRideCard(ride)));
}

function renderRideCard(ride) {
  const template = document.querySelector("#rideCardTemplate");
  const card = template.content.firstElementChild.cloneNode(true);
  const rider = findRider(ride.riderId);

  card.querySelector(".ride-id").textContent = ride.id;
  card.querySelector("h3").textContent = `${ride.pickup} to ${ride.drop}`;

  const statusPill = card.querySelector(".status-pill");
  statusPill.textContent = labelStatus(ride.status);
  statusPill.classList.add(ride.status);

  card.querySelector(".ride-details").innerHTML = detailsHtml([
    ["Passenger", `${ride.passengerName} (${ride.passengerPhone})`],
    ["Time", ride.time],
    ["Fare", `Rs ${ride.fare}`],
    ["Distance", `${ride.distance} km`],
    ["Rider", rider ? `${rider.name} (${rider.phone})` : "Not assigned"],
    ["Notes", ride.notes || "-"]
  ]);

  const assignRow = card.querySelector(".assign-row");
  assignRow.append(createRiderSelect(ride));
  assignRow.append(actionButton("Assign", () => assignSelectedRider(ride.id, card)));
  assignRow.append(actionButton("Done", () => updateRideStatus(ride.id, "completed")));
  assignRow.append(actionButton("Cancel", () => updateRideStatus(ride.id, "cancelled"), "ghost"));

  const messageActions = card.querySelector(".message-actions");
  messageActions.append(actionButton("Copy rider group msg", () => copyText(riderGroupMessage(ride)), "secondary"));
  messageActions.append(actionButton("Copy passenger msg", () => copyText(passengerMessage(ride)), "secondary"));
  messageActions.append(actionButton("Copy rider msg", () => copyText(riderMessage(ride)), "secondary"));

  if (ride.passengerPhone) {
    messageActions.append(whatsAppButton("WhatsApp passenger", ride.passengerPhone, passengerMessage(ride)));
  }

  return card;
}

function createRiderSelect(ride) {
  const select = document.createElement("select");
  select.dataset.rideId = ride.id;
  select.setAttribute("aria-label", `Assign rider for ${ride.id}`);

  const emptyOption = document.createElement("option");
  emptyOption.value = "";
  emptyOption.textContent = "Choose rider";
  select.append(emptyOption);

  state.riders
    .filter((rider) => rider.available || rider.id === ride.riderId)
    .forEach((rider) => {
      const option = document.createElement("option");
      option.value = rider.id;
      option.textContent = `${rider.name} - ${rider.base}`;
      option.selected = rider.id === ride.riderId;
      select.append(option);
    });

  return select;
}

function actionButton(label, handler, variant = "primary") {
  const button = document.createElement("button");
  button.type = "button";
  button.className = `button small ${variant}`;
  button.textContent = label;
  button.addEventListener("click", handler);
  return button;
}

function whatsAppButton(label, phone, message) {
  const link = document.createElement("a");
  link.className = "button small secondary";
  link.textContent = label;
  link.target = "_blank";
  link.rel = "noreferrer";
  link.href = `https://wa.me/${normalizePhone(phone)}?text=${encodeURIComponent(message)}`;
  return link;
}

function assignSelectedRider(rideId, card) {
  const select = card.querySelector(`select[data-ride-id="${rideId}"]`);
  const ride = state.rides.find((item) => item.id === rideId);
  if (!ride || !select.value) return;

  ride.riderId = select.value;
  ride.status = "assigned";
  saveState();
  render();
}

function updateRideStatus(rideId, status) {
  const ride = state.rides.find((item) => item.id === rideId);
  if (!ride) return;
  ride.status = status;
  saveState();
  render();
}

function riderGroupMessage(ride) {
  return [
    `${ride.id}`,
    `Pickup: ${ride.pickup}`,
    `Drop: ${ride.drop}`,
    `Time: ${ride.time}`,
    `Fare: Rs ${ride.fare}`,
    `Passenger: ${ride.seats}`,
    ride.notes ? `Notes: ${ride.notes}` : "",
    `Reply: ACCEPT ${ride.id}`
  ].filter(Boolean).join("\n");
}

function passengerMessage(ride) {
  const rider = findRider(ride.riderId);
  if (!rider) {
    return `We received your ride request ${ride.id}. We are checking nearby riders and will update you shortly.`;
  }

  return [
    `Ride confirmed: ${ride.id}`,
    `Rider: ${rider.name}`,
    `Phone: ${rider.phone}`,
    `Bike: ${rider.vehicle}`,
    `Pickup: ${ride.pickup}`,
    `Drop: ${ride.drop}`,
    `Fare: Rs ${ride.fare}`,
    `Time: ${ride.time}`
  ].join("\n");
}

function riderMessage(ride) {
  const rider = findRider(ride.riderId);
  if (!rider) return `Assign a rider before sending ${ride.id}.`;

  return [
    `${ride.id} assigned to ${rider.name}`,
    `Passenger: ${ride.passengerName}`,
    `Phone: ${ride.passengerPhone}`,
    `Pickup: ${ride.pickup}`,
    `Drop: ${ride.drop}`,
    `Fare: Rs ${ride.fare}`,
    `Time: ${ride.time}`,
    `Reply DONE ${ride.id} after trip.`
  ].join("\n");
}

async function copyText(text) {
  await navigator.clipboard.writeText(text);
}

function suggestFare() {
  const km = Number(els.distance.value || 0);
  if (!km) return;
  const base = 20;
  const perKm = km <= 6 ? 9 : 8;
  const fare = Math.max(40, Math.round((base + km * perKm) / 10) * 10);
  els.fare.value = fare;
}

function loadDemoData() {
  state = {
    nextRideNumber: 105,
    activeFilter: "all",
    riders: [
      { id: "r1", name: "Ramesh", phone: "9000000001", base: "Bellary Bus Stand", vehicle: "KA34 AB 1234", available: true },
      { id: "r2", name: "Mahesh", phone: "9000000002", base: "Kudatini Road", vehicle: "KA34 CD 4567", available: true },
      { id: "r3", name: "Iqbal", phone: "9000000003", base: "Hospital Circle", vehicle: "KA34 EF 7890", available: false }
    ],
    rides: [
      makeDemoRide("R104", "Suresh", "9111111111", "Bellary Bus Stand", "Village X", "Now", 10, 80, "new", ""),
      makeDemoRide("R103", "Lakshmi", "9222222222", "Village Y", "District Hospital", "10:30 AM", 14, 120, "assigned", "r2"),
      makeDemoRide("R102", "Anil", "9333333333", "Market Road", "Village Z", "11:00 AM", 8, 80, "completed", "r1")
    ]
  };
  saveState();
  render();
}

function makeDemoRide(id, passengerName, passengerPhone, pickup, drop, time, distance, fare, status, riderId) {
  return {
    id,
    createdAt: new Date().toISOString(),
    passengerName,
    passengerPhone,
    pickup,
    drop,
    time,
    distance,
    fare,
    seats: 1,
    notes: "",
    status,
    riderId
  };
}

function exportCsv() {
  const header = ["Ride ID", "Status", "Passenger", "Phone", "Pickup", "Drop", "Time", "Distance", "Fare", "Rider", "Rider Phone"];
  const rows = state.rides.map((ride) => {
    const rider = findRider(ride.riderId);
    return [
      ride.id,
      ride.status,
      ride.passengerName,
      ride.passengerPhone,
      ride.pickup,
      ride.drop,
      ride.time,
      ride.distance,
      ride.fare,
      rider?.name || "",
      rider?.phone || ""
    ];
  });

  const csv = [header, ...rows].map((row) => row.map(csvCell).join(",")).join("\n");
  const blob = new Blob([csv], { type: "text/csv" });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = "ride-dispatch.csv";
  link.click();
  URL.revokeObjectURL(url);
}

function clearCompleted() {
  state.rides = state.rides.filter((ride) => ride.status !== "completed" && ride.status !== "cancelled");
  saveState();
  render();
}

function findRider(riderId) {
  return state.riders.find((rider) => rider.id === riderId);
}

function labelStatus(status) {
  return {
    new: "New",
    assigned: "Assigned",
    completed: "Done",
    cancelled: "Cancelled"
  }[status] || status;
}

function detailsHtml(items) {
  return items.map(([key, value]) => `<dt>${escapeHtml(key)}</dt><dd>${escapeHtml(String(value))}</dd>`).join("");
}

function emptyNode(text) {
  const div = document.createElement("div");
  div.className = "empty";
  div.textContent = text;
  return div;
}

function clean(value) {
  return String(value || "").trim();
}

function escapeHtml(value) {
  return value.replace(/[&<>"']/g, (char) => ({
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#039;"
  }[char]));
}

function normalizePhone(phone) {
  const digits = String(phone).replace(/\D/g, "");
  if (digits.length === 10) return `91${digits}`;
  return digits;
}

function csvCell(value) {
  return `"${String(value ?? "").replaceAll('"', '""')}"`;
}
