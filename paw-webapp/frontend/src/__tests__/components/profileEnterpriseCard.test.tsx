import { render, screen } from "@testing-library/react";
import ProfileEnterpriseCard from "../../components/cards/profileEnterpriseCard";

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


const mockUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockUsedNavigate,
}));


describe('Test Job Offer Discover Card', ()=>{

    test('Test that card renders correctly for a given enterprise', ()=>{
        render(<ProfileEnterpriseCard enterprise={enterprise}/>)
        expect(screen.getByText(enterprise.name)).toBeInTheDocument()
    })

    // test('Test that all information is visible', ()=>{
    //     render(<ProfileEnterpriseCard enterprise={enterprise}/>)
    //     expect(screen.getByText(enterprise.name)).toBeInTheDocument()
    //     expect(screen.getByText(enterprise.location)).toBeInTheDocument()
    //     expect(screen.getByText(enterprise.workers)).toBeInTheDocument()
    //     expect(screen.getByText(enterprise.year)).toBeInTheDocument()
    //     expect(screen.getByText(enterprise.website)).toBeInTheDocument()
    //     expect(screen.getByText(enterprise.description)).toBeInTheDocument()
    // })
})