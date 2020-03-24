package me.enterman


import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.util.Duration
import me.enterman.Styles.Companion.loginScreen
//import no.tornado.fxsample.login.Styles.Companion.loginScreen
import tornadofx.*

class LoginScreen : View("请登录") {
    val loginController: LoginController by inject()

    private val model = object : ViewModel() {
        val username = bind { SimpleStringProperty() }
        val password = bind { SimpleStringProperty() }
        val remember = bind { SimpleBooleanProperty() }
    }

    override val root = form {
        addClass(loginScreen)
        fieldset {
            field("用户名") {
                textfield(model.username) {
                    required()
                    whenDocked { requestFocus() }
                }
            }
            field("密码") {
                passwordfield(model.password).required()
            }

        }

        button("Login") {
            isDefaultButton = true

            action {
                model.commit {
                    loginController.tryLogin(
                            model.username.value,
                            model.password.value,
                            model.remember.value
                    )
                }
            }
        }
    }

    override fun onDock() {
        model.validate(decorateErrors = false)
    }

    fun shakeStage() {
        val shakeTransition:ShakeTransition = ShakeTransition(FX.primaryStage, EventHandler {  })
        shakeTransition.playFromStart()
    }

    fun clear() {
        model.username.value = ""
        model.password.value = ""
        model.remember.value = false
    }
}