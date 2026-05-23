package com.ruralbikepool.prototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RuralBikePoolTheme {
                PrototypeApp()
            }
        }
    }
}

private enum class Screen {
    Launch,
    PhoneLogin,
    Otp,
    RoleSelection,
    PassengerHome,
    SearchResults,
    RideDetails,
    RequestPending,
    ConfirmedTrip,
    Rating,
    RiderHome,
    RiderVerification,
    CreateRide,
    IncomingRequest,
    ActiveTrip
}

private data class RideOffer(
    val riderName: String,
    val rating: String,
    val bikeNumber: String,
    val departure: String,
    val fare: String,
    val seats: String,
    val route: String
)

private val sampleRide = RideOffer(
    riderName = "Ramesh",
    rating = "4.8",
    bikeNumber = "KA34 AB 1234",
    departure = "Today, 8:10 AM",
    fare = "Rs 80",
    seats = "1 seat left",
    route = "Village X to Bellary Bus Stand"
)

@Composable
private fun PrototypeApp() {
    var screen by remember { mutableStateOf(Screen.Launch) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppColors.Background
    ) {
        when (screen) {
            Screen.Launch -> LaunchScreen(
                onContinue = { screen = Screen.PhoneLogin }
            )
            Screen.PhoneLogin -> PhoneLoginScreen(
                onBack = { screen = Screen.Launch },
                onSendOtp = { screen = Screen.Otp }
            )
            Screen.Otp -> OtpScreen(
                onBack = { screen = Screen.PhoneLogin },
                onVerify = { screen = Screen.RoleSelection }
            )
            Screen.RoleSelection -> RoleSelectionScreen(
                onPassenger = { screen = Screen.PassengerHome },
                onRider = { screen = Screen.RiderHome }
            )
            Screen.PassengerHome -> PassengerHomeScreen(
                onBack = { screen = Screen.RoleSelection },
                onSearch = { screen = Screen.SearchResults }
            )
            Screen.SearchResults -> SearchResultsScreen(
                onBack = { screen = Screen.PassengerHome },
                onViewRide = { screen = Screen.RideDetails }
            )
            Screen.RideDetails -> RideDetailsScreen(
                onBack = { screen = Screen.SearchResults },
                onRequestSeat = { screen = Screen.RequestPending }
            )
            Screen.RequestPending -> RequestPendingScreen(
                onCancel = { screen = Screen.SearchResults },
                onMockAccept = { screen = Screen.ConfirmedTrip }
            )
            Screen.ConfirmedTrip -> ConfirmedTripScreen(
                onBackHome = { screen = Screen.PassengerHome },
                onRate = { screen = Screen.Rating }
            )
            Screen.Rating -> RatingScreen(
                onSubmit = { screen = Screen.PassengerHome }
            )
            Screen.RiderHome -> RiderHomeScreen(
                onBack = { screen = Screen.RoleSelection },
                onVerify = { screen = Screen.RiderVerification },
                onCreateRide = { screen = Screen.CreateRide },
                onRequests = { screen = Screen.IncomingRequest }
            )
            Screen.RiderVerification -> RiderVerificationScreen(
                onBack = { screen = Screen.RiderHome },
                onSubmit = { screen = Screen.RiderHome }
            )
            Screen.CreateRide -> CreateRideScreen(
                onBack = { screen = Screen.RiderHome },
                onPublish = { screen = Screen.RiderHome }
            )
            Screen.IncomingRequest -> IncomingRequestScreen(
                onBack = { screen = Screen.RiderHome },
                onAccept = { screen = Screen.ActiveTrip },
                onReject = { screen = Screen.RiderHome }
            )
            Screen.ActiveTrip -> ActiveTripScreen(
                onComplete = { screen = Screen.RiderHome }
            )
        }
    }
}

@Composable
private fun LaunchScreen(onContinue: () -> Unit) {
    ScreenFrame {
        Spacer(modifier = Modifier.weight(1f))
        Badge(text = "Bellary rural pilot")
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Rural Bike Pool",
            fontSize = 34.sp,
            lineHeight = 38.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Trusted village bike rides with verified local bike owners.",
            color = AppColors.Muted,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        Spacer(Modifier.height(28.dp))
        PrimaryButton(text = "Continue with phone", onClick = onContinue)
        Spacer(Modifier.height(14.dp))
        Text(text = "Language: Kannada | English", color = AppColors.Muted)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PhoneLoginScreen(onBack: () -> Unit, onSendOtp: () -> Unit) {
    FormScreen(title = "Enter mobile number", subtitle = "We will send an OTP to verify your phone.", onBack = onBack) {
        StaticTextField(label = "Mobile number", value = "+91 9876543210")
        PrimaryButton(text = "Send OTP", onClick = onSendOtp)
        FinePrint("By continuing, you agree to safe and respectful ride sharing.")
    }
}

@Composable
private fun OtpScreen(onBack: () -> Unit, onVerify: () -> Unit) {
    FormScreen(title = "Verify OTP", subtitle = "Sent to +91 9876543210", onBack = onBack) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(6) { index ->
                OtpBox(number = if (index < 4) "${index + 1}" else "")
            }
        }
        PrimaryButton(text = "Verify", onClick = onVerify)
        Text(text = "Resend in 30s", color = AppColors.Muted)
    }
}

@Composable
private fun RoleSelectionScreen(onPassenger: () -> Unit, onRider: () -> Unit) {
    ScreenFrame {
        Header(title = "How do you want to use it?", subtitle = "You can switch later from your profile.")
        SelectCard(
            title = "I need a ride",
            subtitle = "Search village routes and request a seat",
            onClick = onPassenger
        )
        SelectCard(
            title = "I own a bike",
            subtitle = "Share planned rides and accept requests",
            onClick = onRider
        )
    }
}

@Composable
private fun PassengerHomeScreen(onBack: () -> Unit, onSearch: () -> Unit) {
    FormScreen(title = "Good morning, Suresh", subtitle = "Find a bike pool ride", onBack = onBack) {
        StaticTextField(label = "Pickup", value = "Village X temple")
        StaticTextField(label = "Drop", value = "Bellary Bus Stand")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Chip(text = "Now", selected = true)
            Chip(text = "Today", selected = false)
            Chip(text = "Tomorrow", selected = false)
        }
        StaticTextField(label = "Seats", value = "1")
        PrimaryButton(text = "Search rides", onClick = onSearch)
        SectionTitle("Recent routes")
        InfoRow(title = "Village X to Bellary", value = "Usually Rs 70 - Rs 90")
    }
}

@Composable
private fun SearchResultsScreen(onBack: () -> Unit, onViewRide: () -> Unit) {
    FormScreen(title = "Village X to Bellary", subtitle = "Today around 8:00 AM", onBack = onBack) {
        RideOfferCard(offer = sampleRide, onView = onViewRide)
        RideOfferCard(
            offer = sampleRide.copy(
                riderName = "Mahesh",
                rating = "4.6",
                bikeNumber = "KA34 CD 4567",
                departure = "Today, 8:30 AM",
                fare = "Rs 70"
            ),
            onView = onViewRide
        )
        OutlinedButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Create request if no ride fits")
        }
    }
}

@Composable
private fun RideDetailsScreen(onBack: () -> Unit, onRequestSeat: () -> Unit) {
    FormScreen(title = "Ride details", subtitle = sampleRide.route, onBack = onBack) {
        DetailCard {
            InfoRow(title = "Rider", value = "${sampleRide.riderName} - ${sampleRide.rating} star")
            InfoRow(title = "Bike", value = sampleRide.bikeNumber)
            InfoRow(title = "Departure", value = sampleRide.departure)
            InfoRow(title = "Fare contribution", value = sampleRide.fare)
            InfoRow(title = "Seats", value = sampleRide.seats)
        }
        DetailCard {
            SectionTitle("Safety")
            InfoRow(title = "Verification", value = "Approved rider")
            InfoRow(title = "Phone", value = "Visible after request is accepted")
            InfoRow(title = "Support", value = "Report issue available after match")
        }
        PrimaryButton(text = "Request seat", onClick = onRequestSeat)
    }
}

@Composable
private fun RequestPendingScreen(onCancel: () -> Unit, onMockAccept: () -> Unit) {
    ScreenFrame {
        Header(title = "Request sent", subtitle = "Waiting for rider response.")
        DetailCard {
            InfoRow(title = "Route", value = sampleRide.route)
            InfoRow(title = "Fare", value = sampleRide.fare)
            InfoRow(title = "Rider", value = sampleRide.riderName)
        }
        PrimaryButton(text = "Mock rider accepts", onClick = onMockAccept)
        TextButton(onClick = onCancel) {
            Text("Cancel request")
        }
    }
}

@Composable
private fun ConfirmedTripScreen(onBackHome: () -> Unit, onRate: () -> Unit) {
    ScreenFrame {
        Header(title = "Ride confirmed", subtitle = "Contact details are now visible.")
        DetailCard {
            InfoRow(title = "Rider", value = "Ramesh - 4.8 star")
            InfoRow(title = "Phone", value = "9876500000")
            InfoRow(title = "Bike", value = "KA34 AB 1234")
            InfoRow(title = "Pickup", value = "Village X temple")
            InfoRow(title = "Drop", value = "Bellary Bus Stand")
            InfoRow(title = "Fare", value = "Rs 80")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecondaryButton(text = "Call rider", modifier = Modifier.weight(1f))
            SecondaryButton(text = "WhatsApp", modifier = Modifier.weight(1f))
        }
        SecondaryButton(text = "Share trip")
        SecondaryButton(text = "Report issue")
        PrimaryButton(text = "Mock complete and rate", onClick = onRate)
        TextButton(onClick = onBackHome) {
            Text("Back home")
        }
    }
}

@Composable
private fun RatingScreen(onSubmit: () -> Unit) {
    ScreenFrame {
        Header(title = "How was your ride?", subtitle = "Your rating helps keep the network trusted.")
        Text(text = "★ ★ ★ ★ ★", fontSize = 34.sp, color = AppColors.Amber)
        StaticTextField(label = "Comment", value = "On time and polite")
        PrimaryButton(text = "Submit rating", onClick = onSubmit)
    }
}

@Composable
private fun RiderHomeScreen(
    onBack: () -> Unit,
    onVerify: () -> Unit,
    onCreateRide: () -> Unit,
    onRequests: () -> Unit
) {
    var available by remember { mutableStateOf(true) }

    FormScreen(title = "Rider mode", subtitle = "Share seats on your regular route.", onBack = onBack) {
        DetailCard {
            InfoRow(title = "Verification", value = "Approved")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Availability", fontWeight = FontWeight.Bold)
                Switch(checked = available, onCheckedChange = { available = it })
            }
        }
        PrimaryButton(text = "Create planned ride", onClick = onCreateRide)
        SecondaryButton(text = "Complete verification", onClick = onVerify)
        SectionTitle("Today's rides")
        InfoRow(title = "Village X to Bellary 8:10 AM", value = "1 passenger - Rs 80")
        SectionTitle("Incoming requests")
        SelectCard(title = "2 pending requests", subtitle = "Review passengers", onClick = onRequests)
    }
}

@Composable
private fun RiderVerificationScreen(onBack: () -> Unit, onSubmit: () -> Unit) {
    FormScreen(title = "Rider verification", subtitle = "Submit documents for admin review.", onBack = onBack) {
        StaticTextField(label = "Bike number", value = "KA34 AB 1234")
        UploadRow("Profile photo")
        UploadRow("Driving license")
        UploadRow("RC document")
        UploadRow("Insurance")
        StaticTextField(label = "Base village", value = "Village X")
        PrimaryButton(text = "Submit for review", onClick = onSubmit)
    }
}

@Composable
private fun CreateRideScreen(onBack: () -> Unit, onPublish: () -> Unit) {
    FormScreen(title = "Create ride", subtitle = "Post a planned bike pool ride.", onBack = onBack) {
        StaticTextField(label = "From", value = "Village X")
        StaticTextField(label = "To", value = "Bellary Bus Stand")
        StaticTextField(label = "Departure", value = "Today 8:10 AM")
        StaticTextField(label = "Seats", value = "1")
        StaticTextField(label = "Fare", value = "Rs 80")
        Badge(text = "Suggested fare: Rs 70 - Rs 90")
        PrimaryButton(text = "Publish ride", onClick = onPublish)
    }
}

@Composable
private fun IncomingRequestScreen(onBack: () -> Unit, onAccept: () -> Unit, onReject: () -> Unit) {
    FormScreen(title = "Passenger request", subtitle = "Request for your 8:10 AM ride.", onBack = onBack) {
        DetailCard {
            InfoRow(title = "Passenger", value = "Suresh - 4.6 star")
            InfoRow(title = "Pickup", value = "Village X temple")
            InfoRow(title = "Drop", value = "Bellary Bus Stand")
            InfoRow(title = "Fare", value = "Rs 80")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PrimaryButton(text = "Accept", modifier = Modifier.weight(1f), onClick = onAccept)
            OutlinedButton(
                onClick = onReject,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Reject")
            }
        }
    }
}

@Composable
private fun ActiveTripScreen(onComplete: () -> Unit) {
    ScreenFrame {
        Header(title = "Active trip", subtitle = "Passenger details are available after acceptance.")
        DetailCard {
            InfoRow(title = "Passenger", value = "Suresh")
            InfoRow(title = "Phone", value = "9876543210")
            InfoRow(title = "Pickup", value = "Village X temple")
            InfoRow(title = "Drop", value = "Bellary Bus Stand")
            InfoRow(title = "Fare", value = "Rs 80")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecondaryButton(text = "Call", modifier = Modifier.weight(1f))
            SecondaryButton(text = "WhatsApp", modifier = Modifier.weight(1f))
        }
        PrimaryButton(text = "Start trip", onClick = {})
        PrimaryButton(text = "Complete trip", onClick = onComplete)
    }
}

@Composable
private fun ScreenFrame(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        content = content
    )
}

@Composable
private fun FormScreen(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    ScreenFrame {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }
        Header(title = title, subtitle = subtitle)
        content()
    }
}

@Composable
private fun Header(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = title, fontSize = 28.sp, lineHeight = 32.sp, fontWeight = FontWeight.Black)
        Text(text = subtitle, color = AppColors.Muted, lineHeight = 22.sp)
    }
}

@Composable
private fun PrimaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Green)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SecondaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun StaticTextField(label: String, value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun SelectCard(title: String, subtitle: String, onClick: () -> Unit) {
    DetailCard(modifier = Modifier.clickable(onClick = onClick)) {
        Text(title, fontWeight = FontWeight.Black, fontSize = 18.sp)
        Spacer(Modifier.height(4.dp))
        Text(subtitle, color = AppColors.Muted)
    }
}

@Composable
private fun RideOfferCard(offer: RideOffer, onView: () -> Unit) {
    DetailCard {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(offer.riderName, fontWeight = FontWeight.Black, fontSize = 18.sp)
                Text("${offer.rating} star - ${offer.bikeNumber}", color = AppColors.Muted)
            }
            Badge(text = offer.fare)
        }
        HorizontalDivider(color = AppColors.Line)
        InfoRow(title = "Departure", value = offer.departure)
        InfoRow(title = "Seats", value = offer.seats)
        PrimaryButton(text = "View", onClick = onView)
    }
}

@Composable
private fun DetailCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
private fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(title, color = AppColors.Muted, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.9f))
        Spacer(Modifier.width(12.dp))
        Text(value, modifier = Modifier.weight(1.1f), textAlign = TextAlign.End)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontWeight = FontWeight.Black,
        color = AppColors.Ink
    )
}

@Composable
private fun Chip(text: String, selected: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) AppColors.Ink else Color.White)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(text = text, color = if (selected) Color.White else AppColors.Ink, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun Badge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.Green.copy(alpha = 0.12f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = AppColors.Green, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
private fun OtpBox(number: String) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(number, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun UploadRow(label: String) {
    DetailCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontWeight = FontWeight.Bold)
            Badge(text = "Upload")
        }
    }
}

@Composable
private fun FinePrint(text: String) {
    Text(text = text, color = AppColors.Muted, fontSize = 12.sp, lineHeight = 18.sp)
}

@Composable
private fun RuralBikePoolTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = androidx.compose.material3.lightColorScheme(
            primary = AppColors.Green,
            secondary = AppColors.Amber,
            background = AppColors.Background,
            surface = Color.White,
            onPrimary = Color.White,
            onBackground = AppColors.Ink,
            onSurface = AppColors.Ink
        ),
        content = content
    )
}

private object AppColors {
    val Background = Color(0xFFF8F6F1)
    val Green = Color(0xFF1F7A4D)
    val Amber = Color(0xFFB86812)
    val Ink = Color(0xFF222326)
    val Muted = Color(0xFF686B73)
    val Line = Color(0xFFE0DAD0)
}

@Preview(showBackground = true)
@Composable
private fun PrototypePreview() {
    RuralBikePoolTheme {
        PrototypeApp()
    }
}
