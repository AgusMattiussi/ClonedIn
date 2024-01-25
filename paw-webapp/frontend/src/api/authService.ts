import { HttpStatusCode } from "axios"
import { useRequestApi } from "./apiRequest"

export function useLogin() {
  const { loading, apiRequest } = useRequestApi()

  async function loginHandler(username: string, password: string) {
    const response = await apiRequest({
      url: "/auth/access-token",
      method: "POST",
      requiresAuth: true,
      credentials: {
        username,
        password,
      },
    })
    return response.status === HttpStatusCode.Ok
  }

  return { loading, loginHandler }
}

export function useRegisterUser() {
  const { loading, apiRequest } = useRequestApi()

  async function registerHandler(
    email: string,
    password: string,
    repeatPassword: string,
    name: string,
    city: string,
    position: string,
    aboutMe: string,
    category: string,
    level: string,
  ) {
    const response = await apiRequest({
      url: "/users",
      method: "POST",
      body: {
        email,
        password,
        repeatPassword,
        name,
        city,
        position,
        aboutMe,
        category,
        level,
      },
    })
    return response.status !== HttpStatusCode.BadRequest
  }

  return { loading, registerHandler }
}

export function useRegisterEnterprise() {
  const { loading, apiRequest } = useRequestApi()

  async function registerHandler(
    email: string,
    password: string,
    repeatPassword: string,
    name: string,
    city: string,
    category: string,
    workers: string,
    year: string,
    link: string,
    aboutUs: string,
  ) {
    const response = await apiRequest({
      url: "/enterprises",
      method: "POST",
      body: {
        email,
        password,
        repeatPassword,
        name,
        city,
        category,
        workers,
        year,
        link,
        aboutUs,
      },
    })
    console.log(response)
    return response.status !== HttpStatusCode.BadRequest
  }

  return { loading, registerHandler }
}
