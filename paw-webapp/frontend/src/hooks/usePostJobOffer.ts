import { useRequestApi } from "../api/apiRequest"

export const usePostJobOffer = () => {
  const { apiRequest } = useRequestApi()

  async function addJobOffer(
    id: string | undefined,
    position: string,
    description: string,
    salary: string,
    category: string,
    modality: string,
    skill1: string,
    skill2: string,
    skill3: string,
    skill4: string,
  ) {
    const response = await apiRequest({
      url: `/jobOffers`,
      method: "POST",
      body: {
        position,
        description,
        salary,
        category,
        modality,
        skill1,
        skill2,
        skill3,
        skill4,
      },
    })
    return response
  }

  return { addJobOffer }
}
