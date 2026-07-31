import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.indieplaybook.app.root.App
import com.indieplaybook.app.util.LocalNativeViewFactory
import com.indieplaybook.app.util.NativeViewFactory
import com.indieplaybook.app.util.SwiftLibDependencyFactory
import com.indieplaybook.app.util.swiftLibDependenciesModule
import org.koin.core.KoinApplication
import platform.UIKit.UIViewController

fun MainViewController(nativeViewFactory: NativeViewFactory): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
        App()
    }
}

// This is called on application started on Swift side
fun KoinApplication.provideSwiftLibDependencyFactory(factory: SwiftLibDependencyFactory) = run { modules(swiftLibDependenciesModule(factory)) }
