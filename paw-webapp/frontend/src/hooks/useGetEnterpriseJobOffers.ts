import { useRequestApi } from "../api/apiRequest"

export const useGetEnterpriseJobOffers = () => {
  const { apiRequest } = useRequestApi()

  async function getEnterpriseJobOffers(id: string | undefined, queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/jobOffers?enterpriseId=${id}`,
      method: "GET",
      queryParams: queryParams,
      requiresAuth: true,
    })
    return response
  }

  return { getEnterpriseJobOffers }
}