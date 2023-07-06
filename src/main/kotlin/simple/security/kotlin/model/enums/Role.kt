package simple.security.kotlin.model.enums

enum class Role(
    val opcao: String
) {
    USER("USER") {
        override fun opcaoApi() = "USER"
    },

    ADMIN("ADMIN") {
        override fun opcaoApi() = "ADMIN"
    };

    abstract fun opcaoApi(): String
}