export const BASE_URL = "http://localhost:8080/webapp_war"

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

export enum FilledBy {
  ENTERPRISE = 0,
  USER = 1,
  ANY = 2,
}

export const AUTHORIZATION_HEADER = "x-access-token"

export const monthNames = [
  "No-especificado",
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
