package eco.point.ecopoint.ui.activities.exchange

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import eco.point.data.firebase.database.user.FBUserRepository
import eco.point.domain.models.Announcement
import eco.point.domain.models.enums.TagKeys
import eco.point.ecopoint.R
import eco.point.ecopoint.ui.activities.exchange.creator.CreatorActivity
import eco.point.ecopoint.ui.activities.exchange.ui.theme.EcoPointTheme
import eco.point.ecopoint.ui.activities.exchange.ui.theme.darkGreen
import eco.point.ecopoint.ui.activities.exchange.ui.theme.green
import eco.point.ecopoint.ui.activities.exchange.ui.theme.lightGreen
import eco.point.ecopoint.ui.activities.exchange.viewAnnouncement.AnnouncementViewerActivity
import java.util.*

class AnnouncementsActivity : ComponentActivity() {
    var isBanned = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        extras?.let { isBanned = it.getBoolean("isBanned") }
        setContent {
            EcoPointTheme {
                window.statusBarColor = darkGreen.toArgb()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(isBanned)
                }
            }
        }
    }
}

@Composable
fun Greeting(isBanned: Boolean) {
    val viewModel = viewModel(AnnouncementsViewModel::class.java)
    if (viewModel.allAnts.isEmpty())
        viewModel.getAnnouncements(isBanned = isBanned){}
    val wasteTypes by viewModel.wasteTypesStates.observeAsState()
    val antsTypes by viewModel.antsTypesStates.observeAsState()
    val filteredAnts by viewModel.filteredAnts.observeAsState()
    val myAntsState by viewModel.myAntsState.observeAsState()
    Box(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(0.dp, 200.dp, 0.dp, 0.dp)){
            item{
                filteredAnts!!.forEach{
                    AnnouncementPreview(announcement = it)
                }
            }
        }
        Column(modifier = Modifier
            .padding(0.dp, 8.dp, 0.dp, 0.dp)
            .height(192.dp)
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(20.dp))
            .background(
//                    brush = Brush.verticalGradient(colors = listOf(Color.Gray, Color.White)),
                color = Color.White,
                shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp),
            )
        ){}
        Column(modifier = Modifier
            .background(
                color = Color.White, shape = RoundedCornerShape(
                    bottomEnd = 20.dp, bottomStart = 20.dp
                )
            )
            .height(200.dp)
            .fillMaxWidth()) {
            Text(text = stringResource(id = R.string.ants_waste_types), color = green,
                 modifier = Modifier.padding(20.dp, 14.dp, 0.dp, 0.dp), fontSize = 12.sp)
            LazyRow {
                item {
                    FilterButton(
                        text = stringResource(id = R.string.map_butt_plastic),
                        icon = painterResource(id = R.drawable.ic_filter_plastic_active),
                        state = wasteTypes!![TagKeys.PLASTIC.key] == true,
                    ){ viewModel.setAnnouncementsVisibility(TagKeys.PLASTIC.key){} }
                    FilterButton(
                        text = stringResource(id = R.string.map_butt_glass),
                        icon = painterResource(id = R.drawable.ic_filter_glass_active),
                        state = wasteTypes!![TagKeys.GLASS.key] == true
                    ){ viewModel.setAnnouncementsVisibility(TagKeys.GLASS.key){} }
                    FilterButton(
                        text = stringResource(id = R.string.map_butt_metal),
                        icon = painterResource(id = R.drawable.ic_filter_metal_active),
                        state = wasteTypes!![TagKeys.METAL.key] == true
                    ){ viewModel.setAnnouncementsVisibility(TagKeys.METAL.key){} }
                    FilterButton(
                        text = stringResource(id = R.string.map_butt_paper),
                        icon = painterResource(id = R.drawable.ic_filter_paper_active),
                        state = wasteTypes!![TagKeys.PAPER.key] == true
                    ){ viewModel.setAnnouncementsVisibility(TagKeys.PAPER.key){} }
                    FilterButton(
                        text = stringResource(id = R.string.map_butt_food),
                        icon = painterResource(id = R.drawable.ic_filter_food_active),
                        state = wasteTypes!![TagKeys.FOOD.key] == true
                    ){ viewModel.setAnnouncementsVisibility(TagKeys.FOOD.key){} }
                    FilterButton(
                        text = stringResource(id = R.string.map_butt_battery),
                        icon = painterResource(id = R.drawable.ic_filter_battery_active),
                        state = wasteTypes!![TagKeys.BATTERY.key] == true
                    ){ viewModel.setAnnouncementsVisibility(TagKeys.BATTERY.key){} }
                }
            }
            Text(text = stringResource(id = R.string.ants_ants_types), color = green,
                 modifier = Modifier.padding(20.dp, 0.dp), fontSize = 12.sp)
            LazyRow {
                item {
                    FilterButton(
                        text = stringResource(id = R.string.announcement_buy),
                        icon = null,
                        state = antsTypes!![Announcement.BUYER] == true
                    ){ viewModel.setAnnouncementsVisibility(Announcement.BUYER){} }
                    FilterButton(
                        text = stringResource(id = R.string.announcement_sell),
                        icon = null,
                        state = antsTypes!![Announcement.SELLER] == true
                    ){ viewModel.setAnnouncementsVisibility(Announcement.SELLER){} }
                    if (!isBanned)
                    FilterButton(
                        text = stringResource(id = R.string.ants_my_ants),
                        icon = null,
                        state = myAntsState == true
                    ){ viewModel.setAnnouncementsVisibility(AnnouncementsViewModel.MY){} }
                }
            }
        }
    }
    if (!isBanned)
        Box(contentAlignment = Alignment.BottomEnd){
            val context = LocalContext.current
            FloatingActionButton(containerColor = green, contentColor = Color.White,
                                 modifier = Modifier.padding(20.dp, 24.dp),
                                 onClick = { context.startActivity(Intent(
                                     context, CreatorActivity::class.java
                                 )) }) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "")
                    Text(text = stringResource(id = R.string.announcements_butt_create),
                         fontSize = 14.sp, modifier = Modifier.padding(10.dp))
                }
            }
        }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EcoPointTheme {
        Greeting(false)
    }
}

@Composable
fun FilterButton(text: String, icon: Painter?, state: Boolean, callback: () -> Unit){
    Button(onClick = callback,
           modifier = Modifier
               .padding(10.dp)
               .shadow(1.dp, shape = RoundedCornerShape(15.dp))
               .height(40.dp),
           colors = ButtonDefaults.buttonColors(
               containerColor = if (state) green else Color.White,
               contentColor = if (state) darkGreen else lightGreen)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.let { Icon(painter = icon, contentDescription = "") }
            Text(text = text, Modifier.padding(10.dp, 0.dp),
                 textAlign = TextAlign.Center, fontSize = 12.sp)
        }
    }
}

@Composable
fun AnnouncementPreview(announcement: Announcement){
    val context = LocalContext.current
    Column(modifier = Modifier
        .padding(16.dp)
        .shadow(5.dp, shape = RoundedCornerShape(10.dp))
        .background(color = lightGreen, shape = RoundedCornerShape(10.dp))
        .clickable { context.startActivity(Intent(context,
                                                  AnnouncementViewerActivity::class.java).apply {
            putExtra("id", announcement.id)
            val uid = announcement.creator.id
            putExtra("uId", uid)
            putExtra("Reportable", uid != FBUserRepository().uid)
        }) }
        .padding(16.dp)) {
        val locale = context.resources.configuration.locale
        val months = stringArrayResource(R.array.Months)
        val dateString = if (locale.language == "en"){
            months[announcement.dateTime.get(GregorianCalendar.MONTH)] +
                    " " + announcement.dateTime.get(GregorianCalendar.DAY_OF_MONTH).toString() +
                    ", " + announcement.dateTime.get(GregorianCalendar.YEAR)
        } else {
            announcement.dateTime.get(GregorianCalendar.DAY_OF_MONTH).toString() +
                    " " + months[announcement.dateTime.get(GregorianCalendar.MONTH)] +
                    " " + announcement.dateTime.get(GregorianCalendar.YEAR)
        }
        Text(text = dateString, modifier = Modifier
            .fillMaxWidth().padding(0.dp), textAlign = TextAlign.End, color = green, fontSize = 12.sp)
        Text(text = announcement.title, fontWeight = FontWeight.Bold,
             modifier = Modifier.padding(0.dp),
             maxLines = 1, color = darkGreen, fontSize = 16.sp)
        Text(text = announcement.description, maxLines = 1, fontSize = 14.sp,
             modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp), color = darkGreen)
        Row(modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 11.dp), verticalAlignment = Alignment.CenterVertically){
            Text(text = if (announcement.participant == 1)
                stringResource(R.string.announcement_sell)
            else stringResource(R.string.announcement_buy), color = darkGreen,
                 modifier = Modifier
                     .padding(0.dp, 0.dp, 16.dp, 0.dp)
                     .background(color = green, shape = RoundedCornerShape(10.dp))
                     .padding(6.dp),
            fontSize = 12.sp)
            Text(text = "${announcement.cost} ${announcement.units}",
                 color = darkGreen, fontSize = 12.sp)
        }
        Row{
            if (announcement.tag.contains(TagKeys.PLASTIC.key))
                Image(painter = painterResource(id = R.drawable.ic_plastic_idle),
                      contentDescription = "", modifier = Modifier
                        .padding(5.dp)
                        .height(36.dp)
                        .width(36.dp))
            if (announcement.tag.contains(TagKeys.GLASS.key))
                Image(painter = painterResource(id = R.drawable.ic_glass_idle),
                      contentDescription = "", modifier = Modifier
                        .padding(5.dp)
                        .height(36.dp)
                        .width(36.dp))
            if (announcement.tag.contains(TagKeys.METAL.key))
                Image(painter = painterResource(id = R.drawable.ic_metal_idle),
                      contentDescription = "", modifier = Modifier
                        .padding(5.dp)
                        .height(36.dp)
                        .width(36.dp))
            if (announcement.tag.contains(TagKeys.PAPER.key))
                Image(painter = painterResource(id = R.drawable.ic_paper_idle),
                      contentDescription = "", modifier = Modifier
                        .padding(5.dp)
                        .height(36.dp)
                        .width(36.dp))
            if (announcement.tag.contains(TagKeys.FOOD.key))
                Image(painter = painterResource(id = R.drawable.ic_food_idle),
                      contentDescription = "", modifier = Modifier
                        .padding(5.dp)
                        .height(36.dp)
                        .width(36.dp))
            if (announcement.tag.contains(TagKeys.BATTERY.key))
                Image(painter = painterResource(id = R.drawable.ic_battery_idle),
                      contentDescription = "", modifier = Modifier
                        .padding(5.dp)
                        .height(36.dp)
                        .width(36.dp))
        }
    }
}