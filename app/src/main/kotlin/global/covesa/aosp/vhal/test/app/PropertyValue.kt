package global.covesa.aosp.vhal.test.app

data class PropertyValue<T : Any>(val definition: PropertyDefinition<T>,
								  val value: T? = null,
								  val canRead: Boolean = false,
								  val canWrite: Boolean = false,
								  val hasError: Boolean = false) {
	fun format(): String = if (value == null) "---" else when (value) {
		is Float -> listOfNotNull("%.1f".format(value), definition.units)
				.joinToString(" ")

		is Int -> listOfNotNull("${value}", definition.units)
				.joinToString(" ")

		is Boolean -> if (value as? Boolean == true)
			definition.options.getOrNull(0) ?: "ON" else
			definition.options.getOrNull(1) ?: "OFF"

		else -> value.toString()
	}
}
