import { render, screen } from "@testing-library/react";
import { getContactMockedData, getJobMockedData } from "../../__mocks__/mockedData";
import JobOfferUserCard from "../../components/cards/jobOfferUserCard";

const job = getJobMockedData()
const contact = getContactMockedData()

const mockUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockUsedNavigate,
}));


describe('Test Job Offer User Card', ()=>{

    test('Test that card renders correctly for a given job', ()=>{
        render(<JobOfferUserCard contact={contact} job={job} handler={jest.fn()} setJobOfferId={jest.fn()} applicationsView={false}/>)
        expect(screen.getByText(job.position)).toBeInTheDocument()
    })

    test('Test that all information is visible', ()=>{
        render(<JobOfferUserCard contact={contact} job={job} handler={jest.fn()} setJobOfferId={jest.fn()} applicationsView={false}/>)
        expect(screen.getByText(job.position)).toBeInTheDocument()
        expect(screen.getByText(job.description)).toBeInTheDocument()
        expect(screen.getByText("$" + job.salary)).toBeInTheDocument()
        expect(screen.getByText(job.modality)).toBeInTheDocument()
    })

})