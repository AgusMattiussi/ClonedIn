import { useRequestApi } from "../api/apiRequest"

export const useGetEnterpriseJobOffers = () => {
  const { apiRequest } = useRequestApi()

  async function getEnterpriseJobOffers(id: string | undefined, queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/enterprises/${id}/jobOffers`,
      method: "GET",
      queryParams: queryParams,
    })
    return response
  }

  return { getEnterpriseJobOffers }
}
