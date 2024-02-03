import { useRequestApi } from "../api/apiRequest"

export const useGetUserContacts = () => {
  const { apiRequest } = useRequestApi()

  async function getUserContacts(id: string | undefined, queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/users/${id}/contacts`,
      method: "GET",
      queryParams: queryParams,
      requiresAuth: true,
    })
    return response
  }

  return { getUserContacts }
}
