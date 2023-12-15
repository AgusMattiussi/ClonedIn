import { useState } from "react"

import jwtDecode from "jwt-decode"
import { useBetween } from "use-between"

import { UserRole } from "../utils/constants"

export interface UserInfo {
  sub: string
  exp: number
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
      console.log(jwtDecode(token))
    } else {
      localStorage.removeItem("accessToken")
    }
  }

  const handleLogout = () => {
    setAccessToken(null)
  }

  return {
    getAccessToken,
    setAccessToken,
    userInfo,
    handleLogout,
  }
}

export const useSharedAuth = () => useBetween(useAuth)
