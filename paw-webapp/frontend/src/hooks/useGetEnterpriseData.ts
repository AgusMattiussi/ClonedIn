import { useRequestApi } from "../api/apiRequest"

export const useGetEnterpriseData = () => {
  const { apiRequest } = useRequestApi()

  async function getEnterpriseById(id: string | undefined) {
    const response = await apiRequest({
      url: `/enterprises/${id}`,
      method: "GET",
    })
    return response
  }

  async function getEnterpriseByUrl(enterpriseUrl: string) {
    const response = await apiRequest({
      url: enterpriseUrl,
      method: "GET",
    })
    return response
  }

  async function getEnterpriseJobOffers(id: string | undefined, queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/enterprises/${id}/jobOffers`,
      method: "GET",
      queryParams: queryParams,
    })
    return response
  }

  return { getEnterpriseById, getEnterpriseByUrl, getEnterpriseJobOffers }
}
