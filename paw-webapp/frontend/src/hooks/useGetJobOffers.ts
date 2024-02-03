import { useRequestApi } from "../api/apiRequest"

export const useGetJobOffers = () => {
  const { apiRequest } = useRequestApi()

  async function getJobOffers(queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/jobOffers`,
      method: "GET",
      queryParams: queryParams,
      requiresAuth: true,
    })
    return response
  }

  return { getJobOffers }
}
