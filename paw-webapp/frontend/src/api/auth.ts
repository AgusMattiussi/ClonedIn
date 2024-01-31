import jwtDecode from "jwt-decode"
import { useState } from "react"
import { useBetween } from "use-between"
import { UserRole } from "../utils/constants"

export interface UserInfo {
  sub: string
  iat: number
  iss: string
  exp: number
  token_type: string
  role: UserRole
  id: string
}

const useAuth = () => {
  const [userInfo, setUserInfo] = useState<UserInfo | null>(() => {
    const token = localStorage.getItem("accessToken")
    if (token) {
      return jwtDecode(token)
    }
    return null
  })

  const getAccessToken = () => {
    return localStorage.getItem("accessToken")
  }

  const setAccessToken = (token: string | null) => {
    if (token) {
      localStorage.setItem("accessToken", JSON.stringify(token))
      setUserInfo(jwtDecode(token))
    } else {
      localStorage.removeItem("accessToken")
    }
  }

  const getRefreshToken = () => {
    return localStorage.getItem("refreshToken")
  }

  const isTokenExpired = (token: string) => {
    try {
      const decoded = jwtDecode(token) as UserInfo
      const currentTime = Math.floor(Date.now() / 1000)
      return decoded.exp < currentTime
    } catch (error) {
      // Error decoding token (invalid token format)
      return true
    }
  }

  const handleLogout = () => {
    localStorage.removeItem("accessToken")
    setUserInfo(null)
  }

  return {
    getAccessToken,
    setAccessToken,
    getRefreshToken,
    isTokenExpired,
    userInfo,
    setUserInfo,
    handleLogout,
  }
}

export const useSharedAuth = () => useBetween(useAuth)
