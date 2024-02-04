import { render, screen } from "@testing-library/react";
import { JobOfferAvailability } from "../../utils/constants";
import JobOfferEnterpriseCard from "../../components/cards/jobOfferEnterpriseCard";
import { getJobMockedData } from "../../__mocks__/mockedData";

const job = getJobMockedData()

const mockUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockUsedNavigate,
}));


describe('Test Job Offer Enterprise Card', ()=>{

    test('Test that card renders correctly for a given job', ()=>{
        render(<JobOfferEnterpriseCard job={job} handleClose={jest.fn()} setJobOfferId={jest.fn()}/>)
        expect(screen.getByText(job.position)).toBeInTheDocument()
    })

    test('Test that all information is visible', ()=>{
        render(<JobOfferEnterpriseCard job={job} handleClose={jest.fn()} setJobOfferId={jest.fn()}/>)
        expect(screen.getByText(job.position)).toBeInTheDocument()
        expect(screen.getByText(job.description)).toBeInTheDocument()
        expect(screen.getByText("$" + job.salary)).toBeInTheDocument()
        expect(screen.getByText(job.modality)).toBeInTheDocument()
    })

    test('Test that job appears as closed when not available', ()=>{
        job.available = JobOfferAvailability.CLOSED
        render(<JobOfferEnterpriseCard job={job} handleClose={jest.fn()} setJobOfferId={jest.fn()}/>)
        expect(screen.getByText("Closed")).toBeInTheDocument()
    })

})