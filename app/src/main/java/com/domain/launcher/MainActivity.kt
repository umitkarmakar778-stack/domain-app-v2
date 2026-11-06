package com.domain.launcher

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val BgDark = Color(0xFF0B0813)
val CardDark = Color(0xFF1A1428)

val themeChoices = listOf(
    "Purple" to Color(0xFF9B5CFF),
    "Red" to Color(0xFFFF4D6D),
    "Green" to Color(0xFF2ECC71),
    "Orange" to Color(0xFFFF9F1C),
    "Pink" to Color(0xFFFF5CC8)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DomainApp() }
    }
}

@Composable
fun DomainApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("domain_prefs", Context.MODE_PRIVATE) }
    var accent by remember { mutableStateOf(Color(prefs.getInt("accent", 0xFF9B5CFF.toInt()))) }
    var tab by remember { mutableStateOf(0) }

    MaterialTheme(colorScheme = darkColorScheme(primary = accent, background = BgDark, surface = CardDark)) {
        Scaffold(
            containerColor = BgDark,
            bottomBar = { BottomBar(tab, accent) { tab = it } }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                when (tab) {
                    0 -> HomeScreen(accent)
                    1 -> SimpleScreen("Library")
                    2 -> SimpleScreen("Social")
                    3 -> SimpleScreen("News")
                    else -> ProfileScreen(accent) { picked ->
                        accent = picked
                        prefs.edit().putInt("accent", picked.toArgb()).apply()
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(selected: Int, accent: Color, onSelect: (Int) -> Unit) {
    val items: List<Pair<String, ImageVector>> = listOf(
        "Home" to Icons.Filled.Home,
        "Library" to Icons.Filled.VideogameAsset,
        "Social" to Icons.Filled.People,
        "News" to Icons.Filled.Newspaper,
        "Profile" to Icons.Filled.Person
    )
    NavigationBar(containerColor = CardDark) {
        items.forEachIndexed { i, (label, icon) ->
            NavigationBarItem(
                selected = selected == i,
                onClick = { onSelect(i) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = accent,
                    indicatorColor = accent,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

@Composable
fun HomeScreen(accent: Color) {
    val miniGames = listOf("Chess", "Ludo", "Snakes and Ladders", "Carrom", "Card Rush", "Domain Fighters")
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Domain", color = accent, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(20.dp))
            Text("My Games", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Games from your phone will show here. Launching comes in the next step.",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.height(24.dp))
            Text("Browse Games", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(miniGames) { name -> GameCard(name, accent) }
            }
        }
    }
}

@Composable
fun GameCard(name: String, accent: Color) {
    Column(
        Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardDark)
            .padding(12.dp)
    ) {
        Box(
            Modifier.fillMaxWidth().height(80.dp).clip(RoundedCornerShape(12.dp)).background(accent.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.SportsEsports, contentDescription = null, tint = accent)
        }
        Spacer(Modifier.height(8.dp))
        Text(name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text("Offline", color = accent, fontSize = 11.sp)
    }
}

@Composable
fun SimpleScreen(title: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("$title - coming soon", color = Color.Gray, fontSize = 18.sp)
    }
}

@Composable
fun ProfileScreen(accent: Color, onPick: (Color) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Profile", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))
        Text("Theme color", color = Color.White, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            themeChoices.forEach { (_, color) ->
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable { onPick(color) }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Tap a color to change the whole app.", color = Color.Gray, fontSize = 13.sp)
    }
}
