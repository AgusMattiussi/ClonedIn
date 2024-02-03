import { useRequestApi } from "../api/apiRequest"

export const useGetUserById = () => {
  const { apiRequest } = useRequestApi()

  async function getUserById(id: string | undefined) {
    const response = await apiRequest({
      url: `/users/${id}`,
      method: "GET",
      requiresAuth: true,
    })
    return response
  }

  return { getUserById }
}
