Pregunta 1

Cuando llegue el backend REST, lo único que va a cambiar son las clases LibroRepositorioEnMemoria y LectorRepositorioEnMemoria de la capa data, que se van a reemplazar por unas nuevas que hagan las peticiones HTTP, y también el registro de esas clases en AppModule.kt para que Koin inyecte la nueva implementación. Todo lo demás queda intacto: los modelos, las interfaces LibroRepository y LectorRepository, los casos de uso y todas las pantallas con sus ViewModels no se tocan, porque siguiendo la regla de dependencia de Clean Architecture, el dominio y la presentación solo conocen la interfaz del repositorio y no les importa si por debajo hay una lista en memoria o una llamada a una API, así que el cambio queda aislado en una sola capa.

Pregunta 2

Si RegistrarLibroUseCase recibiera anio y ejemplares como Int en vez de String, se perdería la posibilidad de detectar cuando el usuario deja el campo vacío o escribe algo que no es un número, porque un TextField de Compose siempre entrega texto, así que alguien tendría que convertir ese texto a número antes de llegar al caso de uso. Ese "alguien" terminaría siendo la pantalla o el ViewModel, y eso sí sería un problema porque las validaciones del Anexo A (que el año sea un entero, que esté en el rango permitido, que los ejemplares no sean negativos) son reglas de negocio, no de interfaz, y si se hicieran en la pantalla se rompería la separación de capas, sería más difícil probarlas con pruebas unitarias simples y se tendrían que repetir si en el futuro otra pantalla necesita la misma validación.

Pregunta 3

Si LibroRepository estuviera registrado en Koin como factory en vez de single, el usuario notaría que los libros que registra desaparecen o que el catálogo aparece vacío en otras partes de la app, porque Koin crearía una instancia nueva de LibroRepositorioEnMemoria cada vez que un ViewModel la pida, y como esa clase guarda los libros en una lista dentro de la propia instancia, cada instancia nueva tendría su propia lista vacía en lugar de compartir los datos ya registrados. Con single, en cambio, Koin usa siempre la misma instancia para toda la aplicación, así que todos los ViewModels que dependen del repositorio ven el mismo catálogo actualizado.

## Salida de pruebas

Resultado: 29 tests, 0 failed (./gradlew :shared:testAndroidHostTest)


Reusing configuration cache.
> Task :shared:androidPreBuild UP-TO-DATE
> Task :shared:generateAndroidMainAssets UP-TO-DATE
> Task :shared:preAndroidHostTestBuild UP-TO-DATE
> Task :shared:preAndroidMainBuild UP-TO-DATE
> Task :shared:generateAndroidHostTestAssets UP-TO-DATE
> Task :shared:kmpPartiallyResolvedDependenciesChecker
> Task :shared:checkKotlinGradlePluginConfigurationErrors SKIPPED
> Task :shared:convertXmlValueResourcesForAndroidMain NO-SOURCE
> Task :shared:convertXmlValueResourcesForCommonMain NO-SOURCE
> Task :shared:convertXmlValueResourcesForAndroidHostTest NO-SOURCE
> Task :shared:copyNonXmlValueResourcesForAndroidMain NO-SOURCE
> Task :shared:copyNonXmlValueResourcesForAndroidHostTest NO-SOURCE
> Task :shared:copyNonXmlValueResourcesForCommonTest NO-SOURCE
> Task :shared:processAndroidHostTestJavaRes NO-SOURCE
> Task :shared:prepareComposeResourcesTaskForAndroidMain NO-SOURCE
> Task :shared:convertXmlValueResourcesForCommonTest NO-SOURCE
> Task :shared:generateResourceAccessorsForAndroidMain NO-SOURCE
> Task :shared:prepareComposeResourcesTaskForCommonTest NO-SOURCE
> Task :shared:prepareComposeResourcesTaskForAndroidHostTest NO-SOURCE
> Task :shared:generateResourceAccessorsForCommonTest NO-SOURCE
> Task :shared:generateResourceAccessorsForAndroidHostTest NO-SOURCE
> Task :shared:generateAndroidMainEmptyResourceFiles
> Task :shared:mapAndroidMainSourceSetPaths
> Task :shared:mapAndroidHostTestSourceSetPaths
> Task :shared:writeAndroidMainAarMetadata
> Task :shared:generateAndroidHostTestResources
> Task :shared:copyNonXmlValueResourcesForCommonMain
> Task :shared:prepareComposeResourcesTaskForCommonMain
> Task :shared:androidJar
> Task :shared:copyAndroidMainComposeResourcesToAndroidAssets
> Task :shared:generateComposeResClass
> Task :shared:generateExpectResourceCollectorsForCommonMain
> Task :shared:generateResourceAccessorsForCommonMain
> Task :shared:mergeAndroidMainAssets
> Task :shared:packageAndroidMainResources
> Task :shared:generateActualResourceCollectorsForAndroidMain
> Task :shared:compileAndroidMainLibraryResources
> Task :shared:processAndroidMainManifest
> Task :shared:mergeAndroidHostTestAssets
> Task :shared:checkAndroidHostTestAarMetadata
> Task :shared:parseAndroidMainLocalResources
> Task :shared:generateAndroidMainRFile
> Task :shared:processAndroidHostTestManifest
> Task :shared:mergeAndroidHostTestResources
> Task :shared:processAndroidHostTestResources
> Task :shared:packageAndroidHostTestForUnitTest
> Task :shared:generateAndroidHostTestConfig

> Task :shared:compileAndroidMain
w: file:///C:/ProyectosAndroid/BiblioMobil/shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/App.kt:29:5 'fun KoinContext(koin: Koin = ..., content: ComposableFunction0<Unit>): Unit' is deprecated. KoinContext is not needed anymore. This can be removed. Compose Koin context is setup with StartKoin().
w: file:///C:/ProyectosAndroid/BiblioMobil/shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/navigation/InicioScreen.kt:28:52 'val Icons.Filled.MenuBook: ImageVector' is deprecated. Use the AutoMirrored version at Icons.AutoMirrored.Filled.MenuBook.
w: file:///C:/ProyectosAndroid/BiblioMobil/shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/navigation/Screen.kt:13:57 'val Icons.Filled.MenuBook: ImageVector' is deprecated. Use the AutoMirrored version at Icons.AutoMirrored.Filled.MenuBook.
w: file:///C:/ProyectosAndroid/BiblioMobil/shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/presentation/inicio/InicioScreen.kt:29:52 'val Icons.Filled.MenuBook: ImageVector' is deprecated. Use the AutoMirrored version at Icons.AutoMirrored.Filled.MenuBook.
w: file:///C:/ProyectosAndroid/BiblioMobil/shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/presentation/libro/LibroScreen.kt:91:39 'val Icons.Filled.MenuBook: ImageVector' is deprecated. Use the AutoMirrored version at Icons.AutoMirrored.Filled.MenuBook.
w: file:///C:/ProyectosAndroid/BiblioMobil/shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/presentation/libro/LibroScreen.kt:99:39 'val Icons.Filled.MenuBook: ImageVector' is deprecated. Use the AutoMirrored version at Icons.AutoMirrored.Filled.MenuBook.

> Task :shared:processAndroidMainJavaRes
> Task :shared:bundleAndroidMainClassesToCompileJar
> Task :shared:bundleAndroidMainClassesToRuntimeJar

> Task :shared:compileAndroidHostTest
w: file:///C:/ProyectosAndroid/BiblioMobil/shared/src/commonTest/kotlin/pe/edu/upeu/bibliomobil/LibroViewModelTest.kt:24:21 This declaration needs opt-in. Its usage should be marked with '@kotlinx.coroutines.ExperimentalCoroutinesApi' or '@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)'
w: file:///C:/ProyectosAndroid/BiblioMobil/shared/src/commonTest/kotlin/pe/edu/upeu/bibliomobil/LibroViewModelTest.kt:24:29 This declaration needs opt-in. Its usage should be marked with '@kotlinx.coroutines.ExperimentalCoroutinesApi' or '@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)'
w: file:///C:/ProyectosAndroid/BiblioMobil/shared/src/commonTest/kotlin/pe/edu/upeu/bibliomobil/LibroViewModelTest.kt:29:21 This declaration needs opt-in. Its usage should be marked with '@kotlinx.coroutines.ExperimentalCoroutinesApi' or '@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)'

> Task :shared:testAndroidHostTest

[Incubating] Problems report is available at: file:///C:/ProyectosAndroid/BiblioMobil/build/reports/problems/problems-report.html

BUILD SUCCESSFUL in 19s
33 actionable tasks: 33 executed
Configuration cache entry reused.