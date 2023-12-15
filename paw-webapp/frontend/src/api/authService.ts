import { HttpStatusCode } from "axios"
import { useRequestApi } from "./apiRequest"

//TODO: remove
import api from "./api"

export function useLogin() {
  const { loading, apiRequest } = useRequestApi()

  async function loginHandler(username: string, password: string) {
    const response = await apiRequest({
      url: "/auth/authenticate",
      method: "POST",
      requiresAuth: true,
      credentials: {
        username,
        password,
      },
    })
    return response.status !== HttpStatusCode.Unauthorized
  }

  return { loading, loginHandler }
}

//TODO: mover a userApi
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

//TODO: mover a enterpriseApi
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
