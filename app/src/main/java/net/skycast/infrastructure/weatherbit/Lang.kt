package net.skycast.infrastructure.weatherbit

import kotlinx.serialization.SerialName

/**
 * enum for parameter lang
 */
enum class Lang(val value: String) {
    @SerialName("ar")
    ar("ar"),
    @SerialName("az")
    az("az"),
    @SerialName("be")
    be("be"),
    @SerialName("bg")
    bg("bg"),
    @SerialName("bs")
    bs("bs"),
    @SerialName("ca")
    ca("ca"),
    @SerialName("cs")
    cs("cs"),
    @SerialName("de")
    de("de"),
    @SerialName("fi")
    fi("fi"),
    @SerialName("fr")
    fr("fr"),
    @SerialName("el")
    el("el"),
    @SerialName("es")
    es("es"),
    @SerialName("et")
    et("et"),
    @SerialName("hr")
    hr("hr"),
    @SerialName("hu")
    hu("hu"),
    @SerialName("id")
    id("id"),
    @SerialName("it")
    `it`("it"),
    @SerialName("is")
    `is`("is"),
    @SerialName("kw")
    kw("kw"),
    @SerialName("nb")
    nb("nb"),
    @SerialName("nl")
    nl("nl"),
    @SerialName("pl")
    pl("pl"),
    @SerialName("pt")
    pt("pt"),
    @SerialName("ro")
    ro("ro"),
    @SerialName("ru")
    ru("ru"),
    @SerialName("sk")
    sk("sk"),
    @SerialName("sl")
    sl("sl"),
    @SerialName("sr")
    sr("sr"),
    @SerialName("sv")
    sv("sv"),
    @SerialName("tr")
    tr("tr"),
    @SerialName("uk")
    uk("uk"),
    @SerialName("zh")
    zh("zh"),
    @SerialName("zh-tw")
    zhMinusTw("zh-tw");

    override fun toString(): String = value
}