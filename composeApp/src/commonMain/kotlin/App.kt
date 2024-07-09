import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import idle_game.composeapp.generated.resources.Res
import idle_game.composeapp.generated.resources.lemoande
import idle_game.composeapp.generated.resources.lemonadestand
import idle_game.composeapp.generated.resources.llemopnade
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import util.Gelds
import util.gelds
import util.toHumanReadableString
import vw.GameViewModel


@Composable
@Preview
fun App() {
    MaterialTheme {
        Screen()
    }
}

@Composable
@Preview
fun Screen() {
    Scaffold(
        content = {
            val coroutineScope = rememberCoroutineScope()
            val viewModel by remember {
                mutableStateOf(
                    GameViewModel(
                        scope = coroutineScope,
                    )
                )
            }
            DisposableEffect(viewModel) {
                onDispose {
                    viewModel.clear()
                }
            }

            val gameState: GameState? by viewModel.gameState.collectAsState()
            val currentMoney: Gelds? by remember(gameState) {
                derivedStateOf { gameState?.stashedMoney }
            }
            var showDialog by remember { mutableStateOf(false) }
            var moneyButtonlevel by remember { mutableStateOf(1)}

            Image(
                painterResource(Res.drawable.llemopnade),
                contentScale = ContentScale.Crop,
                contentDescription = "A square",
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            )


            Column(

                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(rememberScrollState())

            ) {


                Column(

                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            horizontal = 100.dp,
                        ),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(
                        modifier = Modifier.width(165.dp).height(115.dp).padding(20.dp),

                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(255,153,51)),
                        onClick = {}

                    ) {

                        Text("Cash: " + currentMoney?.toHumanReadableString(),
                            style = MaterialTheme.typography.h5)

                    }
                }

                Box(
                    modifier = Modifier.offset(x = 500.dp, y = 100.dp)
                        .background(Color(100, 165, 0), RoundedCornerShape(8.dp))
                )






                Text(
                    "Lemonade.io",
                    style = MaterialTheme.typography.h1,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.padding(18.dp),
                    onClick = { viewModel.reset() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(255, 165, 0),
                        contentColor = Color.White
                    ),
                )
                {
                    Text("Reset Game")
                }

                Button(
                    onClick = {
                        moneyButtonlevel += 1
                    },
                    modifier = Modifier.padding(18.dp).width(10.dp).height(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(255, 153, 51),
                        contentColor = Color.White
                    )
                ) {
                    Text("Upgrade Click")
                }

                gameState?.let { state ->


                    state.availableJobs.forEach { availableJob ->
                        Generator(
                            gameJob = availableJob,
                            alreadyBought = state.workers.any { it.jobId == availableJob.id },
                            onBuy = { viewModel.addWorker(state, availableJob) },
                            onUpgrade = { viewModel.upgradeJob(state, availableJob) }
                        )
                    }


                }
            }

            gameState?.let { state ->
                Image(
                    painterResource(Res.drawable.lemoande),
                    contentDescription = "A square",
                    modifier = Modifier.offset(
                        x = 450.dp, y = 150.dp
                    ).width(470.dp).height(470.dp).clickable {
                        if (moneyButtonlevel == 1) {
                            viewModel.clickMoney(state, 1.gelds)
                        } else if (moneyButtonlevel == 2) {
                            viewModel.clickMoney(state, 3.gelds)
                        } else {
                            viewModel.clickMoney(state, 1000.gelds)
                        }
                    }
                )
            }
        }
    )
}





@Composable
private fun Generator(
    gameJob: GameJob,
    alreadyBought: Boolean,
    modifier: Modifier = Modifier,
    onBuy: () -> Unit = {},
    onUpgrade: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(8.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFFA500), Color(0xFFFFD700)) // Orange to Gold gradient
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Column {
            Text("Classic L. Machine ${gameJob.id}")
            Text("Level: ${gameJob.level.level}")
            Text("Costs: ${gameJob.level.cost.toHumanReadableString()} Money")
            Text("Earns: ${gameJob.level.earn.toHumanReadableString()} Money")
            Text("Duration: ${gameJob.level.duration.inWholeSeconds} Seconds")
        }
        if (!alreadyBought) {
            Button(onClick = onBuy, colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(255, 153, 51),
                contentColor = Color.White)) {
                Text("Buy")
            }
        } else {
            Text("Bought")
        }
        Button(
            onClick = onUpgrade,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(255, 153, 51),
                contentColor = Color.White
            )
        ) {
            Text("Upgrade")
        }
    }
}

@Composable
fun MinimalDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "This is a minimal dialog",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}


