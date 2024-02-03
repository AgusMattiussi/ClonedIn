import { useRequestApi } from "../api/apiRequest"

export const useGetJobOffersForUser = () => {
  const { apiRequest } = useRequestApi()

  async function getUserJobs(id: string | undefined, queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/jobOffers?enterpriseId=${id}`,
      method: "GET",
      queryParams: queryParams,
    })
    return response
  }

  return { getUserJobs }
}
