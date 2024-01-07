package com.infinitepower.newquiz.core.remote_config

import android.content.Context
import android.content.res.XmlResourceParser
import android.util.Log

/**
 * Parser for the defaults XML file.
 *
 * @author Miraziz Yusupov
 */
internal object RemoteConfigXmlParser {
    private const val TAG = "RemoteConfigXmlParser"

    private const val XML_TAG_ENTRY = "entry"
    private const val XML_TAG_KEY = "key"
    private const val XML_TAG_VALUE = "value"

    @Suppress("NestedBlockDepth", "TooGenericExceptionCaught")
    fun parse(
        context: Context,
        xmlResId: Int
    ): Map<String, String> {
        val defaultsMap = mutableMapOf<String, String>()

        try {
            val resources = context.resources

            if (resources == null) {
                Log.e(
                    TAG,
                    "Could not find the resources of the current context while trying to set defaults from an XML."
                )
                return defaultsMap
            }

            val xmlParser = resources.getXml(xmlResId)

            var curTag: String? = null
            var key: String? = null
            var value: String? = null

            var eventType = xmlParser.eventType
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {
                    curTag = xmlParser.name
                } else if (eventType == XmlResourceParser.END_TAG) {
                    if (xmlParser.name == XML_TAG_ENTRY) {
                        if (key != null && value != null) {
                            defaultsMap[key] = value
                        } else {
                            Log.w(
                                TAG,
                                "An entry in the defaults XML has an invalid key and/or value tag."
                            )
                        }
                        key = null
                        value = null
                    }
                    curTag = null
                } else if (eventType == XmlResourceParser.TEXT) {
                    if (curTag != null) {
                        when (curTag) {
                            XML_TAG_KEY -> key = xmlParser.text
                            XML_TAG_VALUE -> value = xmlParser.text
                            else -> Log.w(
                                TAG,
                                "Encountered an unexpected tag while parsing the defaults XML."
                            )
                        }
                    }
                }
                eventType = xmlParser.next()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing remote config defaults XML", e)
        }

        return defaultsMap
    }
}
