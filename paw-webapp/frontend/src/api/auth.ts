import { useState } from "react"

import jwtDecode from "jwt-decode"
import { useBetween } from "use-between"

import { UserRole } from "../utils/constants"

export interface UserInfo {
  sub: string
  exp: number
}

const useAuth = () => {
  const [userInfo, setUserInfo] = useState<UserInfo | null>(() => {
    const token = localStorage.getItem("authToken")?.split(" ")[1]
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

  const callLogout = () => {
    setAccessToken(null)
  }

  return {
    getAccessToken,
    setAccessToken,
    userInfo,
    callLogout,
  }
}

export const useSharedAuth = () => useBetween(useAuth)
