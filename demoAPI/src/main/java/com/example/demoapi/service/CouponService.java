package com.example.demoapi.service;

import com.example.demoapi.dto.ResponseDTO;
import com.example.demoapi.dto.SearchDTO;
import com.example.demoapi.dto.coupon.CouponDTO;
import com.example.demoapi.entities.Coupon;
import com.example.demoapi.repository.CouponRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public interface CouponService {

    int create(CouponDTO couponDTO) throws ParseException;

    void update(CouponDTO couponDTO);

    void delete(int id);

    CouponDTO getOne(int id);

    ResponseDTO<List<CouponDTO>> search(SearchDTO searchDTO);


    @Service
    @Transactional
    class CouponServiceImpl implements CouponService {

        @Autowired
        CouponRepo couponRepo;

        @Override
        public int create(CouponDTO couponDTO) throws ParseException {
            Coupon coupon = new ModelMapper().createTypeMap(CouponDTO.class, Coupon.class)
                    .addMappings(map -> map.skip(Coupon::setExpiredDate)).map(couponDTO);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            coupon.setExpiredDate(sdf.parse(couponDTO.getExpiredDate()));
            couponRepo.save(coupon);
            return coupon.getId();
        }

        @Override
        public void update(CouponDTO couponDTO) {
            Coupon coupon = new ModelMapper().createTypeMap(CouponDTO.class, Coupon.class)
                    .addMappings(map -> map.skip(Coupon::setExpiredDate)).map(couponDTO);
        }

        @Override
        public void delete(int id) {

        }

        @Override
        public CouponDTO getOne(int id) {
            return null;
        }

        @Override
        public ResponseDTO<List<CouponDTO>> search(SearchDTO searchDTO) {
            return null;
        }
    }
}
