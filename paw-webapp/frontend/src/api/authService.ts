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
