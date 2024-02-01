import { useState } from "react"
import { Buffer } from "buffer"
import axios, { AxiosError, AxiosResponse, HttpStatusCode } from "axios"
import { useNavigate } from "react-router-dom"
import { BASE_URL, AUTHORIZATION_HEADER, CUSTOM_RESPONSE_CODE_FOR_INVALID_REFRESH_TOKEN } from "../utils/constants"
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

  const { getAccessToken, setAccessToken } = useSharedAuth()

  const navigate = useNavigate()

  const api = axios.create({
    baseURL: BASE_URL,
    headers: { "Content-Type": "application/json" },
    withCredentials: true,
  })

  async function apiRequest(input: ApiRequestInput): Promise<AxiosResponse> {
    const accessToken = getAccessToken()

    const { url, method, requiresAuth, credentials } = input
    let { headers, body } = input

    if (credentials) {
      const encodedBasic = Buffer.from(`${credentials?.username}:${credentials?.password}`).toString("base64")
      headers = {
        Authorization: `Basic ${encodedBasic}`,
        ...headers,
      }
    } else if (accessToken) {
      headers = {
        Authorization: `Bearer ${accessToken.replace(/"/g, "")}`,
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
        setAccessToken(response.headers[AUTHORIZATION_HEADER])
      }

      if (response.status === CUSTOM_RESPONSE_CODE_FOR_INVALID_REFRESH_TOKEN) {
        navigate("/login")
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

          if (accessToken) {
            setAccessToken(null)
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
          setAccessToken(response.headers[AUTHORIZATION_HEADER])
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
