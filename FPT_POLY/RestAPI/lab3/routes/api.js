var express = require('express');
var router = express.Router();
const Distributors = require('../model/distributors');
const Ftuits = require('../model/fruits');
router.post('/add_distributor', async(req, res)=>{
    try{
        const data = req.body;
        const newDistributors = new Distributors({
            name: data.name
        });
        const result = await newDistributors.save();
        if(result)
        {
            res.json({
                "status" : 200,
                "messenger" : "Them thanh cong",
                "data" : result
            })
        } else {
            res.json({
                "status" : 404,
                "messenger" : "loi, them khong thanh cong",
                "data" : []
            })
        }
    } catch(error){
        console.log(error);
    }
});
router.post('/add_fruit', async(req, res)=>{
    try{
        const data = req.body;
        const newFruit = new Ftuits({
            name: data.name,
            quantity: data.quantity,
            price: data.price,
            status: data.status,
            image: data.image,
            description: data.description,
            id_distributor: data.id_distributor,
        });
        const result = await newFruit.save();
        if(result)
        {
            res.json({
                "status" : 200,
                "messenger" : "Them thanh cong",
                "data" : result
            })
        } else {
            res.json({
                "status" : 404,
                "messenger" : "loi, them khong thanh cong",
                "data" : []
            })
        }
    } catch(error){
        console.log(error);
    }
});
router.get('/get-list-fruit', async(req, res) => {
    try{
        const data = await Ftuits.find().populate('id_distributor');
        res.json({
            "status" : 200,
            "messenger" : "lay danh sach thanh cong",
            "data" : data
        })
    }
    catch(error){
        console.log(error);
    }
});
router.get('/get-list-fruit-id/:id', async(req, res) => {
    try{
        const {id} = req.params
        const data = await Ftuits.findById(id).populate('id_distributor');
        res.json({
            "status" : 200,
            "messenger" : "lay danh sach id thanh cong",
            "data" : data
        })
    }
    catch(error){
        console.log(error);
    }
});
router.put('/update_fruit_id/:id', async(req, res)=>{
    try{
        const {id} = req.params
        const data = req.body;
        const updateFruit = await Ftuits.findById(id)
        let result = null
        if(updateFruit)
        {
           updateFruit.name = data.name ?? updateFruit.name,
           updateFruit.quantity = data.quantity ?? updateFruit.quantity,
           updateFruit.price = data.price ?? updateFruit.price,
           updateFruit.status = data.status ?? updateFruit.status,
           updateFruit.image = data.image ?? updateFruit.image,
           updateFruit.description = data.description ?? updateFruit.description,
           updateFruit.id_distributor = data.id_distributor ?? updateFruit.id_distributor,
           result = await updateFruit.save();
        }
    
        if(result)
        {
            res.json({
                "status" : 200,
                "messenger" : "cap nhat thanh cong",
                "data" : result
            })
        } else {
            res.json({
                "status" : 404,
                "messenger" : "loi, cap nhat khong thanh cong",
                "data" : []
            })
        }
    } catch(error){
        console.log(error);
    }
});
router.delete('/delete-fruit/:id', async (req, res) => {
    try {
      const { id } = req.params;
      const deletedFruit = await Ftuits.findByIdAndDelete(id);
  
      if (!deletedFruit) {
        return res.status(404).json({
          status: 404,
          message: "Không tìm thấy mục hàng với ID đã cho.",
        });
      }
  
      res.json({
        status: 200,
        message: "Xóa mục hàng thành công.",
        data: deletedFruit,
      });
    } catch (error) {
      console.log(error);
      res.status(500).json({
        status: 500,
        message: "Đã xảy ra lỗi trong quá trình xóa mục hàng.",
      });
    }
  });
module.exports = router;
