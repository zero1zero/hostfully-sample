import axios from "axios";

const apiUrl = "http://localhost:8080"

export type Booking = {
    id?: number,
    start: string,
    end: string
}

export default class API {

    delete = async (id: number): Promise<void> => {
        const headers: { [name: string]: string } = {}

        return axios({
            url: `${apiUrl}/booking/${id}`,
            method: 'delete',
            headers: headers,
        })
    }

    all = async (): Promise<Booking[]> => {
        const headers: { [name: string]: string } = {
            'Accept': 'application/json',
        }

        return axios({
            url: `${apiUrl}/booking`,
            method: 'get',
            headers: headers,
        }).then(r => r.data as Booking[])
    }

    create = async (booking: Booking): Promise<Booking> => {
        const headers: { [name: string]: string } = {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        };

        return axios({
            url: `${apiUrl}/booking`,
            method: 'post',
            headers: headers,
            data: JSON.stringify(booking),
        }).then(r => r.data as Booking)
    }
}