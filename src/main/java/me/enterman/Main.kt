package me.enterman

import javafx.stage.Stage
import tornadofx.App
import tornadofx.launch

class Main {
    //var deleteMe: String? = ""
    val DELETEME = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUUGCM0L44TcXhfS3G2dOZYQGPrMN/z4EPhYiIKa94rCziPrzrqduyz2XvSPncu1+dXJ9ah2LIBIErQ0iLYcvzH1i/3K4NF3ZHPuMBik+6CJRG6IK7OA7V/50VW/4PzKkjSAUWV2/RxcO7Td4qGcw1NU4zTqQnbatWKQfU1Rw3DwIDAQAB"
    class LoginApp : App(LoginScreen::class, Styles::class) {
        val loginController: LoginController by inject()

        override fun start(stage: Stage) {
            super.start(stage)
            loginController.init()
        }
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch<LoginApp>(args)
            //Main::class.java.getResourceAsStream("")
            // write your code here
        }
    }
}