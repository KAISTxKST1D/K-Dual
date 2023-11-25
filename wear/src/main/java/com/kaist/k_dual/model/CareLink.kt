package com.kaist.k_dual.model

import java.util.Arrays


object CountryUtils {
    val supportedCountryCodes = arrayOf(
        "dz",
        "ba",
        "eg",
        "za",
        "ca",
        "cr",
        "mx",
        "ma",
        "pa",
        "pr",
        "us",
        "ar",
        "br",
        "cl",
        "co",
        "ve",
        "hk",
        "in",
        "id",
        "il",
        "jp",
        "kw",
        "lb",
        "my",
        "ph",
        "qa",
        "sa",
        "sg",
        "kr",
        "tw",
        "th",
        "tn",
        "tr",
        "ae",
        "vn",
        "at",
        "be",
        "bg",
        "hr",
        "cz",
        "dk",
        "ee",
        "fi",
        "fr",
        "de",
        "gr",
        "hu",
        "is",
        "ie",
        "it",
        "lv",
        "lt",
        "lu",
        "nl",
        "no",
        "pl",
        "pt",
        "ro",
        "ru",
        "rs",
        "sk",
        "si",
        "es",
        "se",
        "ch",
        "ua",
        "gb",
        "au",
        "nz",
        "bh",
        "om",
        "cn",
        "cy",
        "al",
        "am",
        "az",
        "bs",
        "bb",
        "by",
        "bm",
        "bo",
        "kh",
        "do",
        "ec",
        "sv",
        "ge",
        "gt",
        "hn",
        "ir",
        "iq",
        "jo",
        "xk",
        "ly",
        "mo",
        "mk",
        "mv",
        "mt",
        "mu",
        "yt",
        "md",
        "me",
        "na",
        "nc",
        "ni",
        "ng",
        "pk",
        "py",
        "mf",
        "sd",
        "uy",
        "aw",
        "ky",
        "cw",
        "pe"
    )

    fun isSupportedCountry(countryCode: String): Boolean {
        return Arrays.asList(*supportedCountryCodes).contains(countryCode)
    }

    fun isUS(countryCode: String): Boolean {
        return countryCode == "us"
    }

    fun isOutsideUS(countryCode: String): Boolean {
        return !isUS(countryCode)
    }
}

class CareLink {
}