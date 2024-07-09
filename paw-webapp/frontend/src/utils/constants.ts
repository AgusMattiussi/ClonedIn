export const BASE_URL = "http://localhost:8080/api"
//export const BASE_URL = "http://pawserver.it.itba.edu.ar/paw-2022b-4/api"

export enum UserRole {
  USER = "USER",
  ENTERPRISE = "ENTERPRISE",
}

export enum JobOfferStatus {
  PENDING = "pendiente",
  CLOSED = "cerrada",
  CANCELLED = "cancelada",
  ACCEPTED = "aceptada",
  DECLINED = "rechazada",
}

export enum JobOfferAvailability {
  ACTIVE = "Activa",
  CLOSED = "Cerrada",
  CANCELLED = "Cancelada",
}

export enum FilledBy {
  ENTERPRISE = "enterprise",
  USER = "user",
  ANY = "any",
}

export enum SortBy {
  ANY = "any",
  JOB_OFFER_POSITION = "jobOfferPosition",
  USERNAME = "username",
  STATUS = "status",
  DATE_ASC = "dateAsc",
  DATE_DESC = "dateDesc",
  YEARS_OF_EXPERIENCE = "yearsOfExperience",
  DEFAULT = "predeterminado",
  RECENT = "recientes",
  OLDEST = "antiguos",
  SALARY_ASC = "salarioAsc",
  SALARY_DESC = "salarioDesc",
  EXP_ASC = "expAsc",
  EXP_DESC = "expDesc",
  EDUCATION_ASC = "educAsc",
  EDUCATION_DESC = "educDesc",
}

export const AUTHORIZATION_HEADER = "x-access-token"

export const CUSTOM_RESPONSE_CODE_FOR_INVALID_REFRESH_TOKEN = 603

export const monthNames = [
  "Enero",
  "Febrero",
  "Marzo",
  "Abril",
  "Mayo",
  "Junio",
  "Julio",
  "Agosto",
  "Septiembre",
  "Octubre",
  "Noviembre",
  "Diciembre",
]

export const educationLevels = ["Primario", "Secundario", "Terciario", "Graduado", "Postgrado"]
