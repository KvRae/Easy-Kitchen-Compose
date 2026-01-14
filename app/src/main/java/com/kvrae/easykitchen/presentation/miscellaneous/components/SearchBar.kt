
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.utils.SEARCH_BAR_LAYOUT_ROUTE


/**
 * A composable that displays a search bar with a list of items in our case POIs
 * it covers the whole screen and has a text field to search for items and a list of items
 * @param items the list of items to search for
 * @param placeholder the placeholder of the text field
 * @param value the value of the text field
 * @param onTitleChange the callback to be called when the text field value changes
 * @param backgroundColor the background color of the screen
 * **Docs:** [SearchBarLayout Docs](https://karam-mannai.kvrae.tech/MachInNav-Engine-Docs/features/ui_components/#search-bar-layout)
 *
 */
@Composable
fun SearchBarLayout(
    modifier: Modifier = Modifier,
    items: List<String>,
    placeholder: String? = "",
    value: String? = "",
    onTitleChange: (String) -> Unit = {},
    backgroundColor: Color = Color.Transparent,
    navController : NavController
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()

    ) {
        TextField(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .height(56.dp)
                .fillMaxWidth(),
            value = "",
            placeholder = {},
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "ArrowBack Icon"
                    )
                }
            }
        )
        LazyColumn (
            modifier= Modifier
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxSize(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            content = {
                items(2) {
                    ListItem(
                        colors = ListItemDefaults.colors(
                            backgroundColor
                        ),
                        headlineContent = { Text(text = "Searched") }
                    )
                }
            }
        )
    }

}

@Composable
fun SearchBarField(
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    navController : NavController
) {

    Box {
        TextField(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(56.dp))
                .height(56.dp)
                .fillMaxWidth(),
            value = "",
            placeholder = { Text(text = placeholder.orEmpty())},
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search Icon")
            }
        )
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(56.dp))
                .height(56.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate(SEARCH_BAR_LAYOUT_ROUTE)
                }
        )
    }
}

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .height(56.dp),
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(text = "Search ingredients...") },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "Clear Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true
    )
}