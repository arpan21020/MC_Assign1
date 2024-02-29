package com.example.tutorial

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.codingtroops.profilecardlayout.UserProfile
import com.codingtroops.profilecardlayout.userProfileList
import com.example.tutorial.ui.theme.TutorialTheme

val nameList:ArrayList<String> = arrayListOf(
    "John",
    "Michael",
    "Andrew",
    "Diana",
    "Georgia"
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TutorialTheme {
                HomePage()

            }
        }
    }
}


@Composable
fun HomePage(userProfiles:List<UserProfile> = userProfileList){
    val navController= rememberNavController()
    NavHost(navController = navController , startDestination ="user_list" ){
        composable("user_list"){
            MainScreen4(userProfiles,navController)
        }
        composable(route="user_details/{userId}",
        arguments = listOf(navArgument("userId"){
            type= NavType.IntType
        })
        ){navBackStackEntry->
            UserProfileDetailsScreen(navBackStackEntry.arguments!!.getInt("userId"),navController)
        }
    }
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen4(userProfiles:List<UserProfile>,navController:NavHostController? ){
    Scaffold(topBar = { Appbar(title="Users List", icon = Icons.Default.Home,{}) }){
        Surface(
            modifier=Modifier.fillMaxSize(),
            color=Color.LightGray
        ) {
            LazyColumn{
               items(userProfiles){userProfile->
                   ProfileCard(userProfile = userProfile){
                       navController?.navigate("user_details/${userProfile.id}")
                   }
               }
//                ProfileCard(userProfileList[1])

            }
        }
    }


}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserProfileDetailsScreen(userId:Int,navController:NavHostController? ){
    val userProfile= userProfileList.first{userProfile -> userId==userProfile.id }
    Scaffold(topBar = { Appbar(title = "User Profile Details",Icons.Default.ArrowBack,{
        navController?.popBackStack()
    }) }){
        Surface(
            modifier=Modifier.fillMaxSize(),
            color=Color.LightGray
        ) {

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment =  Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,

                ){
                ProfilePicture(userProfile.pictureUrl,userProfile.status,200.dp)
                ProfileContent(userProfile.name,userProfile.status,Alignment.CenterHorizontally)

            }

        }
    }

}
@Composable
fun Appbar(title:String, icon:ImageVector,backAction:()->Unit){
    TopAppBar(
        navigationIcon= {
            Icon(imageVector = icon,"Content Description",
                Modifier.padding(horizontal = 12.dp).clickable(onClick = {backAction.invoke()})
            )
                        },
        title = {Text(title)}

    )
}
@Composable
fun ProfileCard(userProfile: UserProfile,clickAction:()->Unit){
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable(onClick = { clickAction.invoke() })
        ,
        elevation = 8.dp
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,

                ){
            ProfilePicture(userProfile.pictureUrl,userProfile.status,75.dp)
            ProfileContent(userProfile.name,userProfile.status,Alignment.Start)

        }
    }
}

@Composable
fun ProfilePicture(pictureUrl:String,onlineStatus:Boolean,imageSize:Dp){
    Card(
        shape= CircleShape,
        border = BorderStroke(width=2.dp,color=if(onlineStatus)Color.Green else Color.Red),
        modifier = Modifier.padding(16.dp),
        elevation = 4.dp
    ){
        AsyncImage(
            model = pictureUrl,
//            placeholder = painterResource(id = R.drawable.sudoimage),
//            error = painterResource(id = R.drawable.sudoimage),
            contentDescription = "The delasign logo",
            modifier=Modifier.size(imageSize)
        )
//        AsyncImage(model = "",
//            contentDescription = "Content desc",
//            modifier = Modifier.size(72.dp),
//            contentScale = ContentScale.Crop
//        )
//        Image(painter = painterResource(id = drawableId),
//            contentDescription = "Dummy Description",
//            modifier = Modifier.size(72.dp),
//            contentScale = ContentScale.Crop
//        )
    }


}
@Composable
fun ProfileContent(userName:String,onlineStatus: Boolean,alignment:Alignment.Horizontal){
    Column (
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment,
//            .fillMaxWidth()
            ){
        CompositionLocalProvider(LocalContentAlpha provides if(onlineStatus) 1f else ContentAlpha.medium) {

            Text(text = userName,style=MaterialTheme.typography.h5)

        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(text = if(onlineStatus)"Active Now" else "Offline", style = MaterialTheme.typography.body2)

        }

    }
}
@Composable
fun MainScreen3(){
    val greetingListState= remember {
        mutableStateListOf<String>("John","Michael")
    }
    val newNameState= remember {
        mutableStateOf("")
    }
    Column(
        modifier=Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        GreetingList(greetingListState,
        {greetingListState.add(newNameState.value)},
            newNameState.value,
            {new->newNameState.value=new}
        )
    }
}

//@SuppressLint("UnrememberedMutableState")
@Composable
fun GreetingList(nameListState:List<String>,buttonClick:()->Unit,textFieldValue:String,textFieldValueChange:(new:String)->Unit){

    for (name in nameListState){
//        Greeting(name = name)
        Text(text="Hello $name",style=MaterialTheme.typography.h5)
    }
    TextField(value = textFieldValue, onValueChange = textFieldValueChange)
    Button(onClick = buttonClick) {
        Text(text = "Add new Name")
    }
}
@Composable
fun MainScreen(){
    Surface(color= Color.DarkGray,
    modifier = Modifier.fillMaxSize()){
        Row(
            modifier=Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            HorizontalColorBar(color = Color.Blue)
            HorizontalColorBar(color = Color.Red)
            HorizontalColorBar(color = Color.Green)
            HorizontalColorBar(color = Color.Yellow)
            HorizontalColorBar(color = Color.Cyan)

        }


    }
}

@Composable
fun MainScreen2(){
    Surface(color= Color.DarkGray,
        modifier = Modifier.fillMaxSize()){
        Column(
            modifier=Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Row(
                modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                VerticalColorBar(color = Color.LightGray)
                VerticalColorBar(color = Color.Cyan)
            }

            VerticalColorBar(color = Color.Blue)
            VerticalColorBar(color = Color.Red)
            VerticalColorBar(color = Color.Green)
            VerticalColorBar(color = Color.Cyan)
            VerticalColorBar(color = Color.Yellow)

        }


    }
}

@Composable
fun HorizontalColorBar(color:Color){
    Surface(color=color,
        modifier = Modifier
            .wrapContentSize(
                align = Alignment.TopCenter
            )
            .width(50.dp)
            .height(680.dp)
    )

    {
//        Text(text="wrapped content",
////                modifier = Modifier.wrapContentSize(),
//            style=MaterialTheme.typography.h4)
    }
}

@Composable
fun VerticalColorBar(color:Color){
    Surface(color=color,
        modifier = Modifier
            .wrapContentSize(
                align = Alignment.TopCenter
            )
            .width(80.dp)
            .height(80.dp)
    )

    {
//        Text(text="wrapped content",
////                modifier = Modifier.wrapContentSize(),
//            style=MaterialTheme.typography.h4)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!",
        modifier = Modifier
            .height(240.dp)
            .width(80.dp)
//            .fillMaxWidth()
//            .size(180.dp,80.dp)
//            .fillMaxHeight()
    )
}

@Preview(showBackground = true)
@Composable
fun UserProfileDetail() {
    TutorialTheme() {
        UserProfileDetailsScreen(userId = 0,null)

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TutorialTheme() {
        MainScreen4(userProfiles = userProfileList,null)

    }
}
