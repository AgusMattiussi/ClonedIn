import { fireEvent, render, screen } from "@testing-library/react";
import JobOfferDiscoverCard from "../../components/cards/jobOfferDiscoverCard";
import { JobOfferAvailability } from "../../utils/constants";
import { getMockedData } from "../../__mocks__/mockedData";

const job = getMockedData()


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

    test('Test that "View More" button appears when seeMoreView is set to false', ()=>{
        render(<JobOfferDiscoverCard job={job} seeMoreView={false}/>)
        expect(screen.getByText("View More")).toBeInTheDocument()
    })

    test('Test that job appears as closed when not available', ()=>{
        job.available = JobOfferAvailability.CLOSED
        render(<JobOfferDiscoverCard job={job} seeMoreView={true}/>)
        expect(screen.getByText("Closed")).toBeInTheDocument()
    })

})