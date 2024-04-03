package simple.security.kotlin.adapters.enums

enum class Role(
    val option: String
) {
    USER("USER") {
        override fun opcaoApi() = "USER"
    },

    ADMIN("ADMIN") {
        override fun opcaoApi() = "ADMIN"
    };

    abstract fun opcaoApi(): String
}