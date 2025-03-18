import avail from "../models/availabilityModles.js"

import caterers from '../models/catererModels.js';
export const create = async (req, res) => {
    try {
        const catererId = req.params.id;  // Get catererId from the URL
        const { date, time_slot } = req.body;

        // Check if the caterer exists (optional but recommended)
        const catererExists = await caterers.findById(catererId);
        if (!catererExists) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        // Check if availability already exists for the caterer on the same date and time slot
        const availExist = await avail.findOne({ date, time_slot, catererId });
        if (availExist) {
            return res.status(400).json({ message: "Availability already exists for this date and time slot." });
        }

        // Create a new availability record and associate it with the caterer
        const newItem = new avail({
            date,
            time_slot,
            catererId  // Associate availability with the caterer
        });

        // Save the new availability record
        await newItem.save();
        res.status(201).json(newItem);  // Return the created availability record
    } catch (error) {
        res.status(500).json({ error: "Internal Server error: " + error.message });
    }
};


export const fetch = async (req, res) => {
    try {
        const catererId = req.params.id;  // Get catererId from the URL

        // Fetch availability records for the specific caterer
        const availForCaterer = await avail.find({ catererId: catererId });

        if (availForCaterer.length === 0) {
            return res.status(404).json({ message: "No availability found for this caterer." });
        }

        res.status(200).json(availForCaterer);  // Return the list of availability records
    } catch (error) {
        res.status(500).json({ error: "Internal Server error: " + error.message });
    }
};
