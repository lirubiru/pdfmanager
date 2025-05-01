plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":pdfmanager:core"))
}

application {
    mainClass.set("com.pdfmanager.cli.Main")
}