import { useRequestApi } from "../api/apiRequest"

export const useGetEnterpriseContacts = () => {
  const { apiRequest } = useRequestApi()

  async function getEnterpriseContacts(id: string | undefined, queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/enterprises/${id}/contacts`,
      method: "GET",
      queryParams: queryParams,
    })
    return response
  }

  return { getEnterpriseContacts }
}
