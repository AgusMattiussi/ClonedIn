import { JobOfferAvailability } from "../utils/constants";

export function getJobMockedData (options = {}) {
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

export function getEnterpriseMockedData (options = {}) {
  const enterpriseLinks = {
    self: "",
    image: "logo.png",
    category: "Technology"
  }

const categoryLinks = {
    self: ""
  }
  

const category = {
    id: 155,
    name: "Technology",
    links: categoryLinks
}

const enterprise = {
    id: 150,
    name: "IBM",
    email: "hr@ibm.com",
    location: "CABA",
    workers: "501-1000",
    year: 1950,
    website: "www.ibm.com",
    description: "Global technology innovator, leading advances in AI, automation and hybrid cloud solutions that help businesses",
    categoryInfo: category,
    links: enterpriseLinks
}
  return { ...enterprise, ...options };
}