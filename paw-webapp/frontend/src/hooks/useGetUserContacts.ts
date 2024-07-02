import { useRequestApi } from "../api/apiRequest"

export const useGetUserContacts = () => {
  const { apiRequest } = useRequestApi()

  async function getUserContacts(queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/contacts`,
      method: "GET",
      queryParams: queryParams,
      requiresAuth: true,
    })
    return response
  }

  return { getUserContacts }
}