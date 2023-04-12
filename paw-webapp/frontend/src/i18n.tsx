import i18n from "i18next"
import { initReactI18next } from "react-i18next"
import LanguageDetector from "i18next-browser-languagedetector"
import XHR from "i18next-http-backend"

const options = {
  order: ["querystring", "navigator"],
  lookupQuerystring: "lng",
}

i18n
  .use(XHR)
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources: {
      en: { translation: require("./i18n/en/translation.json") },
      es: { translation: require("./i18n/es/translation.json") },
    },
    detection: options,
    fallbackLng: "en",
    supportedLngs: ["en", "es"],
    debug: true,

    interpolation: {
      escapeValue: false, // not needed for react as it escapes by default
    },
  })

export default i18n
