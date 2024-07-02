import { HttpStatusCode } from "axios"
import { useRequestApi } from "./apiRequest"

export function useLogin() {
  const { loading, apiRequest } = useRequestApi()

  async function loginHandler(username: string, password: string) {
    const queryParams: Record<string, string> = {}
    queryParams.pageSize = "1"
    const response = await apiRequest({
      url: "/skills",
      method: "GET",
      requiresAuth: true,
      credentials: {
        username,
        password,
      },
      queryParams: queryParams,
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
    location: string,
    currentPosition: string,
    description: string,
    category: string,
    educationLevel: string,
  ) {
    const response = await apiRequest({
      url: "/users",
      method: "POST",
      body: {
        email,
        password,
        repeatPassword,
        name,
        location,
        currentPosition,
        description,
        category,
        educationLevel,
      },
    })
    console.log(response)
    return response.status === HttpStatusCode.Created
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
    location: string,
    category: string,
    workers: string,
    yearFounded: string,
    website: string,
    description: string,
  ) {
    const response = await apiRequest({
      url: "/enterprises",
      method: "POST",
      body: {
        email,
        password,
        repeatPassword,
        name,
        location,
        category,
        workers,
        yearFounded,
        website,
        description,
      },
    })
    return response.status === HttpStatusCode.Created
  }

  return { loading, registerHandler }
}
