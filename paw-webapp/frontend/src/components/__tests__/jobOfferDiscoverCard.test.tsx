import { fireEvent, render, screen } from "@testing-library/react";
import JobOfferDiscoverCard from "../cards/jobOfferDiscoverCard";

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
    available: "Yes",
    categoryInfo: category,
    links: jobOfferLinks
}


const mockUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockUsedNavigate,
}));


describe('Test Job Offer Discover Card', ()=>{

    test('Test that card renders correctly for a given job', ()=>{
        render(<JobOfferDiscoverCard job={job} seeMoreView={true}/>)
        expect(screen.getByText(job.position)).toBeInTheDocument()
    })

    test('Test that all information is visible', ()=>{
        render(<JobOfferDiscoverCard job={job} seeMoreView={true}/>)
        expect(screen.getByText(job.position)).toBeInTheDocument()
        expect(screen.getByText(job.description)).toBeInTheDocument()
        expect(screen.getByText("$" + job.salary)).toBeInTheDocument()
        expect(screen.getByText(job.modality)).toBeInTheDocument()
    })

})