package com.joetimmins.watchbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.joetimmins.watchbox.ui.theme.WatchboxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchboxTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WatchboxTheme {
        Greeting("Android")
    }
}

private val guardiansOfTheGalaxy2 = ContentCardData(
    title = "Guardians of the Galaxy 2",
    posterUrl = "",
)

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewContentCard() {
    ContentCard(
        cardData = guardiansOfTheGalaxy2,
        modifier = Modifier.width(160.dp)
    ) {}

}

@ExperimentalMaterialApi
@OptIn(ExperimentalCoilApi::class)
@Composable
fun ContentCard(
    cardData: ContentCardData,
    modifier: Modifier = Modifier,
    onContentClick: () -> Unit,
) {
    WatchboxTheme {
        val painter = rememberImagePainter(
            data = cardData.posterUrl,
            builder = {
                size(300, 450)
            }
        )
        val iconPainter = rememberVectorPainter(image = Icons.Sharp.MailOutline)
        Card(onClick = onContentClick, modifier = modifier.aspectRatio(0.5f)) {
            Column(verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = if (cardData.posterUrl.isEmpty()) iconPainter else painter,
                    contentDescription = cardData.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.67f)
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = cardData.title,
                    maxLines = 3,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(8.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

data class ContentCardData(
    val title: String,
    val posterUrl: String,
)

private val fourGuardiansOfTheGalaxyCards = listOf(
    guardiansOfTheGalaxy2,
    guardiansOfTheGalaxy2,
    guardiansOfTheGalaxy2,
    guardiansOfTheGalaxy2,
)

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewContentGrid() {
    ContentGrid(
        allCardData = fourGuardiansOfTheGalaxyCards,
        onContentClick = {}
    )
}

@ExperimentalMaterialApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentGrid(
    allCardData: List<ContentCardData>,
    onContentClick: () -> Unit,
) {
    LazyVerticalGrid(cells = GridCells.Adaptive(120.dp)) {
        items(allCardData) { cardData ->
            ContentCard(
                cardData = cardData,
                modifier = Modifier.padding(8.dp),
                onContentClick = onContentClick
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun SearchScreenContent() {
    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TopAppBar() {
            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle.Default.copy(fontSize = 20.sp),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Row {
            Spacer(modifier = Modifier.weight(1f))
            IconToggleButton(checked = true, onCheckedChange = {}) {
                Text(text = stringResource(R.string.movie))
            }
            IconToggleButton(checked = false, onCheckedChange = {}) {
                Text(text = stringResource(R.string.series))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        ContentGrid(
            allCardData = fourGuardiansOfTheGalaxyCards,
            onContentClick = {},
        )
    }
}
