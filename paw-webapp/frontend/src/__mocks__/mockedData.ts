import { JobOfferAvailability, JobOfferStatus } from "../utils/constants";

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
    password: "ibm1234",
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

export function getUserMockedData (options = {}) {

  const experienceLinks = {
    self: "",
    user: "15"
  }

  const experience = {
    id: 1,
    position: "Apprentice",
    enterpriseName: "JP",
    description: "Nice",
    monthFrom: 1,
    yearFrom: 2020,
    monthTo: 5,
    yearTo: 2020,
    links: experienceLinks
  }

  const skillsLinks = {
    self: ""
  }

  const skills = {
    id: 2,
    description: "French",
    links: skillsLinks
  }

  const userLinks = {
    self: "",
    image: "logo.png",
    category: "Technology",
    experiences: "",
    educations: "",
    skills: "",
  }

const categoryLinks = {
    self: ""
  }
  

const category = {
    id: 155,
    name: "Technology",
    links: categoryLinks
}

const user = {
    id: 15,
    email: "solanselmo@hotmail.com",
    name: "Sol",
    location: "CABA",
    currentPosition: "Assosiate",
    description: "Hardworking",
    educationLevel: "Graduate",
    visibility: 1,
    categoryInfo: category,
    experienceInfo: experience,
    skillsInfo: skills,
    links: userLinks
}
  return { ...user, ...options };
}

export function getContactMockedData (options = {}) {
  const categoryLinks = {
    self: ""
  }
  

  const category = {
      id: 50,
      name: "Technology",
      links: categoryLinks
  }

  const contactLinks = {
    self: "",
    user: "Sol",
    enterprise: "ITBA",
    jobOffer: "Backend Developer",
    userCategory: "Technology"
  }

  const contact = {
    id: 1,
    filledBy: 1,
    status: JobOfferStatus.PENDING,
    date: "28/01/2024",
    userInfo: getUserMockedData(),
    userName: "Sol",
    userYearsOfExp: 3,
    userId: 1,
    categoryInfo: category,
    enterpriseInfo: getEnterpriseMockedData(),
    jobOfferInfo: getJobMockedData(),
    links: contactLinks,
}

  return { ...contact, ...options };
}
