import { useState } from "react"

import jwtDecode from "jwt-decode"
import { useBetween } from "use-between"

import { UserRole } from "../utils/constants"

export interface UserInfo {
  sub: string
  exp: number
  username: string
  email: string
  role: UserRole
  points: number
}

const useAuth = () => {
  const [userInfo, setUserInfo] = useState<UserInfo | null>(() => {
    const token = localStorage.getItem("authToken")?.split(" ")[1]
    if (token) {
      return jwtDecode(token)
    }
    return null
  })

  const getAuthToken = () => {
    return localStorage.getItem("authToken")
  }

  const setAuthToken = (token: string | null) => {
    if (token) {
      localStorage.setItem("authToken", token)
      setUserInfo(jwtDecode(token.split(" ")[1]))
    } else {
      localStorage.removeItem("authToken")
    }
  }

  const callLogout = () => {
    setAuthToken(null)
  }

  return {
    getAuthToken,
    setAuthToken,
    userInfo,
    callLogout,
  }
}

export const useSharedAuth = () => useBetween(useAuth)
