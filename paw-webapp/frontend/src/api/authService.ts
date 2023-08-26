import api from "./api"
import jwtDecode from "jwt-decode"

export interface UserInfo {
  sub: string
  exp: number
}

export const getCurrentUser = () => {
  const token = localStorage.getItem("accessToken")
  if (token) {
    const userInfo: UserInfo = jwtDecode(token)
    return userInfo.sub
  }
  return null
}

export const login = (email: string, password: string) => {
  return api.post("/auth/authenticate", { email, password }).then((response) => {
    if (response.data.accessToken) {
      localStorage.setItem("accessToken", JSON.stringify(response.data))
    }
    return response.data
  })
}

export const getAccessToken = () => {
  return localStorage.getItem("accessToken")
}

export const logout = () => {
  localStorage.removeItem("accessToken")
}

export const registerUser = (
  email: string,
  password: string,
  repeatPassword: string,
  name: string,
  city: string,
  position: string,
  aboutMe: string,
  category: string,
  level: string,
) => {
  return api.post("/users", { email, password, repeatPassword, name, city, position, aboutMe, category, level })
}

export const registerEnterprise = (
  email: string,
  password: string,
  repeatPassword: string,
  name: string,
  city: string,
  workers: string,
  year: string,
  link: string,
  aboutUs: string,
  category: string,
) => {
  return api.post("/enterprises", {
    email,
    password,
    repeatPassword,
    name,
    city,
    workers,
    year,
    link,
    aboutUs,
    category,
  })
}
