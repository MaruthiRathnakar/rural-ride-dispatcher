# Rural Ride Dispatcher

A simple browser-based dispatcher app for a rural bike ride service.

It helps an operator:

- Add passenger ride requests.
- Add verified riders.
- Mark riders available or unavailable.
- Assign riders to rides.
- Copy WhatsApp-ready messages for rider groups, passengers, and assigned riders.
- Export ride data as CSV.

## Run locally

Open `index.html` in a browser, or run a local server:

```sh
python3 -m http.server 4173
```

Then visit:

```text
http://127.0.0.1:4173
```

Data is stored in the browser using `localStorage`.
