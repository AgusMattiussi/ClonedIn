import { JobOfferAvailability } from "../utils/constants";

export function getMockedData (options = {}){
    const categoryLinks = {
        self: ""
      }
      
    
    const category = {
        id: 50,
        name: "Technology",
        links: categoryLinks
    }
    
    const jobOfferLinks = {
        self: "",
        enterprise: "ITBA",
        category: "Technology",
        skills: ""
      }
    
    const job = {
        id: 100,
        position: "Backend Developer",
        description: "We're looking for a backend devleoper to help build application",
        salary: "1000",
        modality: "Remote",
        available: JobOfferAvailability.ACTIVE,
        categoryInfo: category,
        links: jobOfferLinks
    }

    return { ...job, ...options };
}