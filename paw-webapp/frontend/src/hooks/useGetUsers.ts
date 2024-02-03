import { useRequestApi } from "../api/apiRequest"

export const useGetUsers = () => {
  const { apiRequest } = useRequestApi()

  async function getUsers(queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/users`,
      method: "GET",
      queryParams: queryParams,
      requiresAuth: true,
    })
    return response
  }

  return { getUsers }
}
