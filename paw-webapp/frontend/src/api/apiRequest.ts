import { useState } from "react"

import axios, { AxiosError, AxiosResponse, HttpStatusCode } from "axios"
import { Buffer } from "buffer"
import { useNavigate } from "react-router-dom"

import { BASE_URL, AUTHORIZATION_HEADER } from "../utils/constants"

import { useSharedAuth } from "./auth"

export interface BasicCredentials {
  username: string
  password: string
}

export interface ApiRequestInput {
  url: string
  method?: string
  body?: object
  headers?: Record<string, string>
  requiresAuth?: boolean
  credentials?: BasicCredentials
  queryParams?: Record<string, string>
}

export const useRequestApi = () => {
  const [loading, setLoading] = useState(false)

  const { getAuthToken, setAuthToken } = useSharedAuth()

  const navigate = useNavigate()

  const api = axios.create({
    baseURL: BASE_URL,
    headers: { "Content-Type": "application/json" },
  })

  async function apiRequest(input: ApiRequestInput): Promise<AxiosResponse> {
    const authToken = getAuthToken()

    const { url, method, body, requiresAuth, credentials } = input
    let { headers } = input

    if (requiresAuth) {
      if (!authToken && !credentials) {
        navigate("/login", {
          state: { from: window.location.pathname.substring(13) },
        })
        throw new Error("No auth token or credentials. Please login")
      }
    }

    if (credentials) {
      const encodedBasic = Buffer.from(`${credentials?.username}:${credentials?.password}`).toString("base64")
      headers = {
        Authorization: `Basic ${encodedBasic}`,
        ...headers,
      }
    } else if (authToken) {
      headers = {
        Authorization: `${authToken}`,
        ...headers,
      }
    }

    setLoading(true)
    try {
      const response = await api({
        url,
        method: method || "GET",
        data: body,
        headers: {
          ...headers,
        },
        params: input.queryParams,
      })

      if (response.headers[AUTHORIZATION_HEADER]) {
        setAuthToken(response.headers[AUTHORIZATION_HEADER])
      }
      return response
    } catch (err) {
      if (axios.isAxiosError(err)) {
        const axiosError = err as AxiosError

        const response = axiosError.response

        if (response?.status === HttpStatusCode.Unauthorized) {
          if (credentials) {
            return axiosError.response as AxiosResponse // Login
          }
          if (authToken) {
            setAuthToken(null)
          }

          if (headers) {
            delete headers["Authorization"]
          }

          return await apiRequest({
            url,
            method,
            body,
            headers,
            requiresAuth,
          })
        }

        if (response?.headers[AUTHORIZATION_HEADER]) {
          setAuthToken(response.headers[AUTHORIZATION_HEADER])
        }

        if (axiosError.response) {
          return axiosError.response as AxiosResponse
        }
        return {
          status: 503, // Service Unavailable
        } as AxiosResponse
      }
      throw err
    } finally {
      setLoading(false)
    }
  }
  return { loading, apiRequest }
}
