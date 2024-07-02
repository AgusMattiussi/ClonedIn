import { useRequestApi } from "../api/apiRequest"

export const useGetEnterpriseContacts = () => {
  const { apiRequest } = useRequestApi()

  async function getEnterpriseContacts(queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/contacts`,
      method: "GET",
      queryParams: queryParams,
      requiresAuth: true,
    })
    return response
  }

  return { getEnterpriseContacts }
}
